package com.anand.interviewprep.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/** What an admin sends to add a module to a course. */
@Getter
@Setter
public class ModuleRequest {

    @NotBlank
    private String title;

    private int position;
}