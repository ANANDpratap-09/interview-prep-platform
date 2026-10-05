package com.anand.interviewprep.repository;

import com.anand.interviewprep.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Learner-facing browse: only published courses, optionally filtered.
    List<Course> findByPublishedTrue();

    List<Course> findByPublishedTrueAndTopic(String topic);

    List<Course> findByPublishedTrueAndDifficulty(String difficulty);
}