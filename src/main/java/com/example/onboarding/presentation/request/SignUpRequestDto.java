package com.example.onboarding.presentation.request;

import com.example.onboarding.domain.model.User;
import com.example.onboarding.domain.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {
    private String username;
    private String password;
    private String nickname;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .username(this.getUsername())
                .password(encodedPassword)
                .nickname(this.getNickname())
                .authorities(new ArrayList<>())
                .build();
    }
}
