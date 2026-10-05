package com.anand.interviewprep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionSummaryResponse {
    private Long sessionId;
    private String topic;
    private int totalQuestions;
    private int correctCount; // only counts MCQ answers marked correct
    private int score; // percentage, 0-100
}