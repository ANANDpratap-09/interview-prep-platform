package com.anand.interviewprep.repository;

import com.anand.interviewprep.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByTopic(String topic);

    List<Question> findByTopicAndDifficulty(String topic, String difficulty);
}