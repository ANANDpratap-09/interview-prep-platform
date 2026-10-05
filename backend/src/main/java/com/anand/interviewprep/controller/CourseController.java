package com.anand.interviewprep.controller;

import com.anand.interviewprep.dto.CourseDetailResponse;
import com.anand.interviewprep.dto.CourseResponse;
import com.anand.interviewprep.service.CourseService;
import com.anand.interviewprep.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** Learner-facing endpoints: browsing courses and enrolling. All require a valid token (see SecurityConfig). */
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    // GET /api/courses                 -> all published
    // GET /api/courses?topic=Java       -> filtered by topic
    // GET /api/courses?difficulty=EASY  -> filtered by difficulty
    @GetMapping
    public List<CourseResponse> browse(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String difficulty) {
        return courseService.browsePublished(topic, difficulty);
    }

    @GetMapping("/{id}")
    public CourseDetailResponse getDetail(@PathVariable Long id) {
        return courseService.getCourseDetail(id);
    }

    @PostMapping("/{id}/enroll")
    public void enroll(@PathVariable Long id, Authentication authentication) {
        enrollmentService.enroll(authentication.getName(), id);
    }

    @DeleteMapping("/{id}/enroll")
    public void unenroll(@PathVariable Long id, Authentication authentication) {
        enrollmentService.unenroll(authentication.getName(), id);
    }
}