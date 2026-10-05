package com.anand.interviewprep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Shown when browsing the course list — no module/lesson detail. */
@Getter
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private String topic;
    private String difficulty;
    private boolean published;
    private int moduleCount;
}