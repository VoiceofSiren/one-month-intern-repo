package com.example.onboarding.application.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignResponseDto {
    private String token;

    public static SignResponseDto fromString(String accessToken) {
        return SignResponseDto.builder()
                .token(accessToken)
                .build();
    }
}
