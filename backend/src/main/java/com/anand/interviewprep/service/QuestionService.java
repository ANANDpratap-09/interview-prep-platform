package com.anand.interviewprep.service;

import com.anand.interviewprep.dto.QuestionRequest;
import com.anand.interviewprep.dto.QuestionResponse;
import com.anand.interviewprep.entity.Question;
import com.anand.interviewprep.exception.ApiException;
import com.anand.interviewprep.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/** Admin CRUD for the question bank. */
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionResponse create(QuestionRequest request) {
        Question question = Question.builder()
                .topic(request.getTopic())
                .difficulty(request.getDifficulty())
                .type(request.getType())
                .body(request.getBody())
                .options(request.getOptions())
                .correctAnswer(request.getCorrectAnswer())
                .explanation(request.getExplanation())
                .build();
        questionRepository.save(question);
        return toResponse(question);
    }

    public QuestionResponse update(Long id, QuestionRequest request) {
        Question question = findOrThrow(id);
        question.setTopic(request.getTopic());
        question.setDifficulty(request.getDifficulty());
        question.setType(request.getType());
        question.setBody(request.getBody());
        question.setOptions(request.getOptions());
        question.setCorrectAnswer(request.getCorrectAnswer());
        question.setExplanation(request.getExplanation());
        questionRepository.save(question);
        return toResponse(question);
    }

    public void delete(Long id) {
        questionRepository.delete(findOrThrow(id));
    }

    private Question findOrThrow(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ApiException("Question not found", HttpStatus.NOT_FOUND));
    }

    private QuestionResponse toResponse(Question q) {
        return new QuestionResponse(q.getId(), q.getTopic(), q.getDifficulty(), q.getType(),
                q.getBody(), q.getOptions(), q.getCorrectAnswer(), q.getExplanation());
    }
}