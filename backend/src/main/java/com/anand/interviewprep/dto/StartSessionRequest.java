package com.anand.interviewprep.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartSessionRequest {

    @NotBlank
    private String topic;

    private String difficulty; // optional: null means any difficulty

    private int questionCount = 5; // how many questions to pull, defaults to 5
}