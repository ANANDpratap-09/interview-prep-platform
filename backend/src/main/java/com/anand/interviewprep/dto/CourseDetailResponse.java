package com.anand.interviewprep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 * Full course view: used when a learner opens a specific course and needs
 * to see its modules and lessons, not just the summary from CourseResponse.
 */
@Getter
@AllArgsConstructor
public class CourseDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String topic;
    private String difficulty;
    private List<ModuleDetail> modules;

    @Getter
    @AllArgsConstructor
    public static class ModuleDetail {
        private Long id;
        private String title;
        private int position;
        private List<LessonResponse> lessons;
    }
}