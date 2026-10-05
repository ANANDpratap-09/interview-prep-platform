package com.anand.interviewprep.repository;

import com.anand.interviewprep.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}