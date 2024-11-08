package com.example.onboarding.application.dto;

import com.example.onboarding.domain.model.User;
import com.example.onboarding.domain.model.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class SignUpResponseDto {
    private String username;
    private String nickname;
    private List<UserRoleDto> authorities;

    public static SignUpResponseDto fromEntity(User savedUser) {
        return SignUpResponseDto.builder()
                .username(savedUser.getUsername())
                .nickname(savedUser.getNickname())
                .authorities(savedUser.getAuthorities().stream()
                        .map(userRole -> {return UserRoleDto.fromEntity(userRole);})
                        .collect(Collectors.toList()))
                .build();
    }
}
