package com.anand.interviewprep.controller;

import com.anand.interviewprep.dto.*;
import com.anand.interviewprep.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Admin-only endpoints for managing course content.
 * SecurityConfig already restricts "/api/admin/**" to ROLE_ADMIN — this
 * controller doesn't need to check roles itself.
 */
@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;

    @PostMapping
    public CourseResponse create(@Valid @RequestBody CourseRequest request) {
        return courseService.createCourse(request);
    }

    @PutMapping("/{id}")
    public CourseResponse update(@PathVariable Long id, @Valid @RequestBody CourseRequest request) {
        return courseService.updateCourse(id, request);
    }

    @PutMapping("/{id}/publish")
    public CourseResponse publish(@PathVariable Long id) {
        return courseService.togglePublish(id, true);
    }

    @PutMapping("/{id}/unpublish")
    public CourseResponse unpublish(@PathVariable Long id) {
        return courseService.togglePublish(id, false);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @PostMapping("/{courseId}/modules")
    public void addModule(@PathVariable Long courseId, @Valid @RequestBody ModuleRequest request) {
        courseService.addModule(courseId, request);
    }

    @PostMapping("/modules/{moduleId}/lessons")
    public void addLesson(@PathVariable Long moduleId, @Valid @RequestBody LessonRequest request) {
        courseService.addLesson(moduleId, request);
    }
}