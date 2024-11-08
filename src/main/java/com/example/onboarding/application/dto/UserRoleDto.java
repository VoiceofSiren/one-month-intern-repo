package com.example.onboarding.application.dto;

import com.example.onboarding.domain.model.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class UserRoleDto {

    private String authorityName;

    public static UserRoleDto fromEntity(UserRole userRole) {
        return UserRoleDto.builder()
                .authorityName(userRole.getAuthorityName())
                .build();
    }
}
