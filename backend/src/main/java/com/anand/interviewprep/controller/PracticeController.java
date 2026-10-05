package com.anand.interviewprep.controller;

import com.anand.interviewprep.dto.*;
import com.anand.interviewprep.service.PracticeSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** Learner-facing practice session endpoints (FR9-FR12). */
@RestController
@RequestMapping("/api/practice")
@RequiredArgsConstructor
public class PracticeController {

    private final PracticeSessionService practiceSessionService;

    @PostMapping("/sessions")
    public List<SessionQuestionResponse> start(@Valid @RequestBody StartSessionRequest request, Authentication auth) {
        return practiceSessionService.startSession(auth.getName(), request);
    }

    @PostMapping("/sessions/{sessionId}/answers")
    public AnswerResultResponse submitAnswer(
            @PathVariable Long sessionId,
            @Valid @RequestBody SubmitAnswerRequest request,
            Authentication auth) {
        return practiceSessionService.submitAnswer(auth.getName(), sessionId, request);
    }

    @PostMapping("/sessions/{sessionId}/finish")
    public SessionSummaryResponse finish(@PathVariable Long sessionId, Authentication auth) {
        return practiceSessionService.finishSession(auth.getName(), sessionId);
    }
}