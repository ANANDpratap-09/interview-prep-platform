package com.anand.interviewprep.repository;

import com.anand.interviewprep.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    List<LessonProgress> findByUserId(Long userId);

    boolean existsByUserIdAndLessonId(Long userId, Long lessonId);
}