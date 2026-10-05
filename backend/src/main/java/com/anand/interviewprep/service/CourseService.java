package com.anand.interviewprep.service;

import com.anand.interviewprep.dto.*;
import com.anand.interviewprep.entity.Course;
import com.anand.interviewprep.entity.Lesson;
import com.anand.interviewprep.entity.Module;
import com.anand.interviewprep.exception.ApiException;
import com.anand.interviewprep.repository.CourseRepository;
import com.anand.interviewprep.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for courses, modules and lessons. Learner-facing reads
 * only ever see published courses; admin methods work on any course,
 * published or not.
 */
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    // ---- Learner-facing ----

    public List<CourseResponse> browsePublished(String topic, String difficulty) {
        List<Course> courses;
        if (topic != null) {
            courses = courseRepository.findByPublishedTrueAndTopic(topic);
        } else if (difficulty != null) {
            courses = courseRepository.findByPublishedTrueAndDifficulty(difficulty);
        } else {
            courses = courseRepository.findByPublishedTrue();
        }
        return courses.stream().map(this::toCourseResponse).collect(Collectors.toList());
    }

    public CourseDetailResponse getCourseDetail(Long courseId) {
        Course course = findPublishedOrThrow(courseId);

        List<CourseDetailResponse.ModuleDetail> moduleDetails = course.getModules().stream()
                .sorted(Comparator.comparingInt(Module::getPosition))
                .map(m -> new CourseDetailResponse.ModuleDetail(
                        m.getId(),
                        m.getTitle(),
                        m.getPosition(),
                        m.getLessons().stream()
                                .sorted(Comparator.comparingInt(Lesson::getPosition))
                                .map(l -> new LessonResponse(l.getId(), l.getTitle(), l.getContent(), l.getPosition()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new CourseDetailResponse(course.getId(), course.getTitle(), course.getDescription(),
                course.getTopic(), course.getDifficulty(), moduleDetails);
    }

    // ---- Admin ----

    public CourseResponse createCourse(CourseRequest request) {
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .topic(request.getTopic())
                .difficulty(request.getDifficulty())
                .published(false) // new courses start unpublished until admin is ready
                .build();
        courseRepository.save(course);
        return toCourseResponse(course);
    }

    public CourseResponse updateCourse(Long courseId, CourseRequest request) {
        Course course = findAnyOrThrow(courseId);
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTopic(request.getTopic());
        course.setDifficulty(request.getDifficulty());
        courseRepository.save(course);
        return toCourseResponse(course);
    }

    public CourseResponse togglePublish(Long courseId, boolean published) {
        Course course = findAnyOrThrow(courseId);
        course.setPublished(published);
        courseRepository.save(course);
        return toCourseResponse(course);
    }

    public void deleteCourse(Long courseId) {
        Course course = findAnyOrThrow(courseId);
        courseRepository.delete(course); // cascade deletes its modules and lessons too
    }

    public void addModule(Long courseId, ModuleRequest request) {
        Course course = findAnyOrThrow(courseId);
        Module module = Module.builder()
                .title(request.getTitle())
                .position(request.getPosition())
                .course(course)
                .build();
        moduleRepository.save(module);
    }

    public void addLesson(Long moduleId, LessonRequest request) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ApiException("Module not found", HttpStatus.NOT_FOUND));
        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .position(request.getPosition())
                .module(module)
                .build();
        module.getLessons().add(lesson); // cascade on Module -> Lesson saves it too
        moduleRepository.save(module);
    }

    // ---- Helpers ----

    private Course findPublishedOrThrow(Long id) {
        Course course = findAnyOrThrow(id);
        if (!course.isPublished()) {
            throw new ApiException("Course not found", HttpStatus.NOT_FOUND);
        }
        return course;
    }

    private Course findAnyOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ApiException("Course not found", HttpStatus.NOT_FOUND));
    }

    private CourseResponse toCourseResponse(Course c) {
        return new CourseResponse(c.getId(), c.getTitle(), c.getDescription(),
                c.getTopic(), c.getDifficulty(), c.isPublished(), c.getModules().size());
    }
}