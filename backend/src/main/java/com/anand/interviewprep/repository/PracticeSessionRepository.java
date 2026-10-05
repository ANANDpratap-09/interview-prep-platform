package com.anand.interviewprep.repository;

import com.anand.interviewprep.entity.PracticeSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PracticeSessionRepository extends JpaRepository<PracticeSession, Long> {

    List<PracticeSession> findByUserIdAndCompletedTrueOrderByCompletedAtDesc(Long userId);
}