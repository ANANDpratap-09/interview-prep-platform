package com.anand.interviewprep.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/** What an admin sends to create/update a question. */
@Getter
@Setter
public class QuestionRequest {

    @NotBlank
    private String topic;

    @NotBlank
    private String difficulty;

    @NotBlank
    private String type; // "MCQ" or "SHORT_ANSWER"

    @NotBlank
    private String body;

    private String options; // "opt1|opt2|opt3|opt4" for MCQ, omit for SHORT_ANSWER

    @NotBlank
    private String correctAnswer;

    private String explanation;
}