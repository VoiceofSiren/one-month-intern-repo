package com.example.onboarding.presentation.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApiResponse {
    private int status;
    private String message;
    private LocalDateTime timeStamp;

    public static ApiResponse error(int status, String message) {
        return ApiResponse.builder()
                .status(status)
                .message(message)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}