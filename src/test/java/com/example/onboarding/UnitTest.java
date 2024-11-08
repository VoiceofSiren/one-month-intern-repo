package com.example.onboarding;

import com.example.onboarding.application.dto.UserRoleDto;
import com.example.onboarding.application.service.UserService;
import com.example.onboarding.domain.model.User;
import com.example.onboarding.domain.model.UserRole;
import com.example.onboarding.infrastructure.repository.UserRepository;
import com.example.onboarding.infrastructure.repository.UserRoleRepository;
import com.example.onboarding.presentation.request.SignRequestDto;
import com.example.onboarding.presentation.request.SignUpRequestDto;
import com.example.onboarding.presentation.response.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnitTest {

    @InjectMocks private UserService userService;
    @Mock private UserRepository userRepository;
    @Mock private UserRoleRepository userRoleRepository;
    @Mock private PasswordEncoder passwordEncoder;

    private SignUpRequestDto preparedUserDto;

    private User preparedUser;
    private UserRole preparedUserRole;

    @BeforeEach
    void setup() {
        preparedUserDto = new SignUpRequestDto("username1", "password1", "nickname1");

        // Mock된 PasswordEncoder 동작 정의
        when(passwordEncoder.encode(preparedUserDto.getPassword()))
                .thenReturn("encodedPassword1");
        when(passwordEncoder.matches(anyString(), eq("encodedPassword1")))
                .thenReturn(true);

        String encodedPassword = passwordEncoder.encode(preparedUserDto.getPassword());
        preparedUser = preparedUserDto.toEntity(encodedPassword);
        preparedUserRole = UserRole.createUserRole(preparedUser);
    }

    @DisplayName("회원 가입 성공 테스트")
    @Test
    void test1() {

        // given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("JIN HO", "12341234", "Mentos");
        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        // when
        User user = signUpRequestDto.toEntity(encodedPassword);
        UserRole userRole = UserRole.createUserRole(user);
        doReturn(userRole)
                .when(userRoleRepository).save(any(UserRole.class));

        user.getAuthorities().add(userRole);
        doReturn(user)
                .when(userRepository).save(any(User.class));

        // then
        assertThat(userService.createUser(signUpRequestDto).getUsername()).isEqualTo(user.getUsername());
    }

    @DisplayName("회원 가입 실패 테스트 - 중복 username 검증")
    @Test
    void test2() {

        // given
        String requestPassword = "12341234";
        SignUpRequestDto signUpRequestDto1 = new SignUpRequestDto("JIN HO", requestPassword, "Mentos");
        String encodedPassword = passwordEncoder.encode(requestPassword);
        User user1 = signUpRequestDto1.toEntity(encodedPassword);

        doReturn(Optional.of(user1))
                .when(userRepository).findUserByUsername("12341234");

        // when
        SignUpRequestDto signUpRequestDto2 = new SignUpRequestDto("JIN HO", "12345678", "Nick name");
        ApiException exception = assertThrows(ApiException.class, () -> userService.createUser(signUpRequestDto2));

        // then
        assertThat(exception.getMessage()).contains("username이 중복되었습니다.");
    }

    @DisplayName("로그인 성공 테스트")
    @Test
    void test3() {

        // given
        SignRequestDto signRequestDto = new SignRequestDto("username1", "password1");

        // when & then
        assertThat(signRequestDto.getUsername()).isEqualTo(preparedUser.getUsername());
        assertThat(passwordEncoder.matches(signRequestDto.getPassword(), preparedUser.getPassword())).isTrue();
    }

}