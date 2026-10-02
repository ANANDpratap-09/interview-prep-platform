package com.anand.interviewprep.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 * Thrown anywhere in our service layer when something the client did is
 * wrong (duplicate email, bad login, etc). Carries the HTTP status it
 * should map to, so the handler below doesn't have to guess.
 */
@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}