package com.anand.interviewprep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Full question view — used by admin (includes the answer, for editing). */
@Getter
@AllArgsConstructor
public class QuestionResponse {
    private Long id;
    private String topic;
    private String difficulty;
    private String type;
    private String body;
    private String options;
    private String correctAnswer;
    private String explanation;
}