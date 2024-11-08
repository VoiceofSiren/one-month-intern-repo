package com.example.onboarding.presentation.controller;

import com.example.onboarding.application.service.UserService;
import com.example.onboarding.presentation.request.SignRequestDto;
import com.example.onboarding.presentation.request.SignUpRequestDto;
import com.example.onboarding.presentation.response.ApiResponse;
import com.example.onboarding.presentation.response.exception.ApiException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Object signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        try {
            return userService.createUser(signUpRequestDto);
        } catch (ApiException e) {
            return ApiResponse.error(e.getHttpStatus().value(), e.getMessage());
        }
    }

    @PostMapping("/sign")
    public Object sign(@RequestBody SignRequestDto signRequestDto, HttpServletResponse response) {
        try {
            return userService.login(signRequestDto, response);
        } catch (ApiException e) {
            return ApiResponse.error(e.getHttpStatus().value(), e.getMessage());
        }
    }

}
