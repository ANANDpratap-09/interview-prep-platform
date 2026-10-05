package com.anand.interviewprep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
import java.util.Map;

/** The full learner dashboard: FR15 from the SRS. */
@Getter
@AllArgsConstructor
public class DashboardResponse {
    private int enrolledCourseCount;
    private List<CourseProgress> courseProgress;
    private List<SessionSummaryResponse> recentSessions;
    private Map<String, Double> averageScoreByTopic; // topic -> average score %

    @Getter
    @AllArgsConstructor
    public static class CourseProgress {
        private Long courseId;
        private String courseTitle;
        private int totalLessons;
        private int completedLessons;
        private double completionPercent;
    }
}