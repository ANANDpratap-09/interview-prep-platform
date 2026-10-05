package com.anand.interviewprep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LessonResponse {
    private Long id;
    private String title;
    private String content;
    private int position;
}