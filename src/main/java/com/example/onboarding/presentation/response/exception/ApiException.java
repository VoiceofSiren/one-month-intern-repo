package com.example.onboarding.presentation.response.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String logMessage;

    public ApiException(HttpStatus httpStatus, String errorMessage, String logMessage) {
        super(errorMessage);
        this.httpStatus = httpStatus;
        this.logMessage = logMessage;
    }
}
