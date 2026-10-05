package com.anand.interviewprep.controller;

import com.anand.interviewprep.dto.DashboardResponse;
import com.anand.interviewprep.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping("/dashboard")
    public DashboardResponse dashboard(Authentication auth) {
        return progressService.getDashboard(auth.getName());
    }
}