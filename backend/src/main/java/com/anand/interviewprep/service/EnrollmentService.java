package com.anand.interviewprep.service;

import com.anand.interviewprep.entity.Course;
import com.anand.interviewprep.entity.Enrollment;
import com.anand.interviewprep.entity.User;
import com.anand.interviewprep.exception.ApiException;
import com.anand.interviewprep.repository.CourseRepository;
import com.anand.interviewprep.repository.EnrollmentRepository;
import com.anand.interviewprep.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Handles a learner enrolling in or leaving a course. */
@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional // ensures the existence check and the insert happen as one atomic unit
    public void enroll(String userEmail, Long courseId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiException("Course not found", HttpStatus.NOT_FOUND));

        if (enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId)) {
            throw new ApiException("Already enrolled in this course", HttpStatus.CONFLICT);
        }

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .build();
        enrollmentRepository.save(enrollment);
    }

    public void unenroll(String userEmail, Long courseId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new ApiException("Not enrolled in this course", HttpStatus.NOT_FOUND));

        enrollmentRepository.delete(enrollment);
    }
}