package com.example.onboarding.presentation.request;

import com.example.onboarding.domain.model.User;
import com.example.onboarding.domain.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SignRequestDto {
    private String username;
    private String password;
}
