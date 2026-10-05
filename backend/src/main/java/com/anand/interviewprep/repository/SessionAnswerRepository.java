package com.anand.interviewprep.repository;

import com.anand.interviewprep.entity.SessionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionAnswerRepository extends JpaRepository<SessionAnswer, Long> {
}