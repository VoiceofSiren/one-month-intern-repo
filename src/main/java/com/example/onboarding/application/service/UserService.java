package com.example.onboarding.application.service;

import com.example.onboarding.application.dto.SignResponseDto;
import com.example.onboarding.application.dto.SignUpResponseDto;
import com.example.onboarding.domain.exception.NicknameAlreadyExistsException;
import com.example.onboarding.domain.exception.PasswordDoesNotMatchException;
import com.example.onboarding.domain.model.User;
import com.example.onboarding.domain.model.UserRole;
import com.example.onboarding.infrastructure.jwt.JwtUtil;
import com.example.onboarding.infrastructure.repository.UserRepository;
import com.example.onboarding.infrastructure.repository.UserRoleRepository;
import com.example.onboarding.presentation.request.SignRequestDto;
import com.example.onboarding.presentation.request.SignUpRequestDto;
import com.example.onboarding.presentation.response.exception.ApiException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponseDto createUser(SignUpRequestDto signUpRequestDto) {

        // [1] username 중복 여부 검증
        try {
            validateUsernameExistence(signUpRequestDto.getUsername());
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "username이 중복되었습니다.", e.getMessage());
        }

        // [2] password 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        // [3] 회원 가입
        User user = signUpRequestDto.toEntity(encodedPassword);
        UserRole userRole = UserRole.createUserRole(user);
        userRoleRepository.save(userRole);
        user.getAuthorities().add(userRole);
        User savedUser = userRepository.save(user);

        // [4] 응답 반환
        return SignUpResponseDto.fromEntity(savedUser);
    }

    @Transactional
    public SignResponseDto login(SignRequestDto signRequestDto, HttpServletResponse response) {

        // [1] username 일치 여부 검증
        User user = null;
        try {
            user = userRepository.findUserByUsername(signRequestDto.getUsername()).orElseThrow();
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "username이 일치하지 않습니다.", e.getMessage());
        }

        // [2] password 일치 여부 검증
        String password = signRequestDto.getPassword();
        try {
            validatePassword(password, user.getPassword());
        } catch (PasswordDoesNotMatchException e){
            throw new ApiException(HttpStatus.BAD_REQUEST, "password가 일치하지 않습니다.", e.getMessage());
        }

        // [3] login 성공 시 accessToken 발급
        String accessToken = jwtUtil.createToken(user.getId(), user.getAuthorities());
        response.setHeader("Authorization", accessToken);

        // [4] 응답 반환
        return SignResponseDto.fromString(accessToken);

    }

    public void validateUsernameExistence(String username) {
        if (userRepository.findUserByUsername(username).isPresent()) {
            throw new NicknameAlreadyExistsException();
        }
    }

    private void validatePassword(String plainPassword, String encodedPassword) {
        if (!passwordEncoder.matches(plainPassword, encodedPassword)) {
            throw new PasswordDoesNotMatchException();
        }
    }


}
