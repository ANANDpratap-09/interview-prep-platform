package com.anand.interviewprep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Returned right after submitting one answer — reveals the correct answer now. */
@Getter
@AllArgsConstructor
public class AnswerResultResponse {
    private Boolean correct; // null for SHORT_ANSWER (self-graded, see SessionAnswer comment)
    private String correctAnswer;
    private String explanation;
}