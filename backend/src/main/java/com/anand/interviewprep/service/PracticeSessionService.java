package com.anand.interviewprep.service;

import com.anand.interviewprep.dto.*;
import com.anand.interviewprep.entity.*;
import com.anand.interviewprep.exception.ApiException;
import com.anand.interviewprep.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/** Core practice-session flow: start, answer one question at a time, finish with a score (FR9-FR12). */
@Service
@RequiredArgsConstructor
public class PracticeSessionService {

    private final PracticeSessionRepository sessionRepository;
    private final QuestionRepository questionRepository;
    private final SessionAnswerRepository answerRepository;
    private final UserRepository userRepository;

    public List<SessionQuestionResponse> startSession(String userEmail, StartSessionRequest request) {
        User user = findUser(userEmail);

        List<Question> pool = request.getDifficulty() != null
                ? questionRepository.findByTopicAndDifficulty(request.getTopic(), request.getDifficulty())
                : questionRepository.findByTopic(request.getTopic());

        if (pool.isEmpty()) {
            throw new ApiException("No questions available for this topic/difficulty", HttpStatus.NOT_FOUND);
        }

        // Shuffle so repeated sessions on the same topic don't always serve
        // the same questions in the same order.
        Collections.shuffle(pool);
        List<Question> selected = pool.stream()
                .limit(Math.max(1, request.getQuestionCount()))
                .collect(Collectors.toList());

        PracticeSession session = PracticeSession.builder()
                .user(user)
                .topic(request.getTopic())
                .difficulty(request.getDifficulty())
                .build();
        sessionRepository.save(session);

        // We save a placeholder "unanswered" SessionAnswer for each chosen
        // question up front, so we know exactly which questions belong to
        // this session when it's time to score it.
        for (Question q : selected) {
            SessionAnswer placeholder = SessionAnswer.builder()
                    .session(session)
                    .question(q)
                    .build();
            answerRepository.save(placeholder);
        }

        return selected.stream()
                .map(q -> new SessionQuestionResponse(q.getId(), q.getType(), q.getBody(), q.getOptions()))
                .collect(Collectors.toList());
    }

    @Transactional
    public AnswerResultResponse submitAnswer(String userEmail, Long sessionId, SubmitAnswerRequest request) {
        PracticeSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ApiException("Session not found", HttpStatus.NOT_FOUND));
        ensureOwnedBy(session, userEmail);

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ApiException("Question not found", HttpStatus.NOT_FOUND));

        SessionAnswer answer = session.getAnswers().stream()
                .filter(a -> a.getQuestion().getId().equals(question.getId()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Question is not part of this session", HttpStatus.BAD_REQUEST));

        answer.setGivenAnswer(request.getAnswer());

        Boolean isCorrect = null;
        if ("MCQ".equals(question.getType())) {
            // Simple exact-match check, case-insensitive, trimmed.
            isCorrect = question.getCorrectAnswer().trim().equalsIgnoreCase(
                    request.getAnswer() == null ? "" : request.getAnswer().trim());
        }
        answer.setCorrect(isCorrect);
        answerRepository.save(answer);

        return new AnswerResultResponse(isCorrect, question.getCorrectAnswer(), question.getExplanation());
    }

    @Transactional
    public SessionSummaryResponse finishSession(String userEmail, Long sessionId) {
        PracticeSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ApiException("Session not found", HttpStatus.NOT_FOUND));
        ensureOwnedBy(session, userEmail);

        long total = session.getAnswers().size();
        long correctCount = session.getAnswers().stream()
                .filter(a -> Boolean.TRUE.equals(a.getCorrect()))
                .count();

        int score = total == 0 ? 0 : (int) Math.round((correctCount * 100.0) / total);

        session.setScore(score);
        session.setCompleted(true);
        session.setCompletedAt(java.time.LocalDateTime.now());
        sessionRepository.save(session);

        return new SessionSummaryResponse(session.getId(), session.getTopic(), (int) total, (int) correctCount, score);
    }

    private void ensureOwnedBy(PracticeSession session, String userEmail) {
        if (!session.getUser().getEmail().equals(userEmail)) {
            throw new ApiException("This session doesn't belong to you", HttpStatus.FORBIDDEN);
        }
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
    }
}