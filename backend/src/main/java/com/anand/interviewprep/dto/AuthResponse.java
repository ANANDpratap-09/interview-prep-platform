package com.anand.interviewprep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** What we send back after a successful register or login. */
@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private String role;
}