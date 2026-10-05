package com.anand.interviewprep.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/** What an admin sends to add a lesson to a module. */
@Getter
@Setter
public class LessonRequest {

    @NotBlank
    private String title;

    private String content;

    private int position;
}