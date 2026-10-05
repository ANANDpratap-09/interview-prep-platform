package com.anand.interviewprep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * What a learner sees WHILE taking a session — deliberately excludes
 * correctAnswer and explanation, so they can't see the answer before
 * submitting (those are only revealed in AnswerResultResponse after).
 */
@Getter
@AllArgsConstructor
public class SessionQuestionResponse {
    private Long questionId;
    private String type;
    private String body;
    private String options;
}