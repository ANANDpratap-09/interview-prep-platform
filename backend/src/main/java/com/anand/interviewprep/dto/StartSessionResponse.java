package com.anand.interviewprep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class StartSessionResponse {
    private Long sessionId;
    private List<SessionQuestionResponse> questions;
}