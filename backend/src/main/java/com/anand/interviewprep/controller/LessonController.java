package com.anand.interviewprep.controller;

import com.anand.interviewprep.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** Marking a lesson as completed (FR14). */
@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final ProgressService progressService;

    @PostMapping("/{id}/complete")
    public void complete(@PathVariable Long id, Authentication auth) {
        progressService.completeLesson(auth.getName(), id);
    }
}