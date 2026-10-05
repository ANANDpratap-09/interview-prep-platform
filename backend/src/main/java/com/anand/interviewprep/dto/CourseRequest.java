package com.anand.interviewprep.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/** What an admin sends to create or update a course. */
@Getter
@Setter
public class CourseRequest {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String topic;

    @NotBlank
    private String difficulty;
}