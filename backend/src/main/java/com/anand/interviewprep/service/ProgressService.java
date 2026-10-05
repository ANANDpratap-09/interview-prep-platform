package com.anand.interviewprep.service;

import com.anand.interviewprep.dto.DashboardResponse;
import com.anand.interviewprep.dto.SessionSummaryResponse;
import com.anand.interviewprep.entity.*;
import com.anand.interviewprep.exception.ApiException;
import com.anand.interviewprep.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/** Builds the learner dashboard (FR14, FR15) and handles marking lessons complete. */
@Service
@RequiredArgsConstructor
public class ProgressService {

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final LessonRepository lessonRepository;
    private final PracticeSessionRepository sessionRepository;

    public void completeLesson(String userEmail, Long lessonId) {
        User user = findUser(userEmail);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ApiException("Lesson not found", HttpStatus.NOT_FOUND));

        if (lessonProgressRepository.existsByUserIdAndLessonId(user.getId(), lessonId)) {
            return; // already marked complete, nothing to do
        }

        LessonProgress progress = LessonProgress.builder()
                .user(user)
                .lesson(lesson)
                .build();
        lessonProgressRepository.save(progress);
    }

    public DashboardResponse getDashboard(String userEmail) {
        User user = findUser(userEmail);

        List<Enrollment> enrollments = enrollmentRepository.findByUserId(user.getId());
        List<LessonProgress> completedLessons = lessonProgressRepository.findByUserId(user.getId());
        Set<Long> completedLessonIds = completedLessons.stream()
                .map(lp -> lp.getLesson().getId())
                .collect(Collectors.toSet());

        List<DashboardResponse.CourseProgress> courseProgress = enrollments.stream()
                .map(e -> {
                    Course course = e.getCourse();
                    int totalLessons = course.getModules().stream()
                            .mapToInt(m -> m.getLessons().size())
                            .sum();
                    long completed = course.getModules().stream()
                            .flatMap(m -> m.getLessons().stream())
                            .filter(l -> completedLessonIds.contains(l.getId()))
                            .count();
                    double percent = totalLessons == 0 ? 0 : (completed * 100.0) / totalLessons;
                    return new DashboardResponse.CourseProgress(
                            course.getId(), course.getTitle(), totalLessons, (int) completed, percent);
                })
                .collect(Collectors.toList());

        List<PracticeSession> completedSessions =
                sessionRepository.findByUserIdAndCompletedTrueOrderByCompletedAtDesc(user.getId());

        List<SessionSummaryResponse> recentSessions = completedSessions.stream()
                .limit(10)
                .map(s -> new SessionSummaryResponse(
                        s.getId(), s.getTopic(), s.getAnswers().size(),
                        (int) s.getAnswers().stream().filter(a -> Boolean.TRUE.equals(a.getCorrect())).count(),
                        s.getScore() == null ? 0 : s.getScore()))
                .collect(Collectors.toList());

        // Average score per topic, across ALL completed sessions (not just the 10 shown above) —
        // this is what highlights weak topics per FR15.
        Map<String, Double> averageByTopic = completedSessions.stream()
                .collect(Collectors.groupingBy(
                        PracticeSession::getTopic,
                        Collectors.averagingInt(s -> s.getScore() == null ? 0 : s.getScore())
                ));

        return new DashboardResponse(enrollments.size(), courseProgress, recentSessions, averageByTopic);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
    }
}