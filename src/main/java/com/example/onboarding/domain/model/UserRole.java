package com.example.onboarding.domain.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Entity
@Table(name = "tb_user_role")
public class UserRole {

    @Id
    @GeneratedValue
    @Column(name = "user_role_id")
    private Long id;

    @Column(name = "authority_name")
    private String authorityName;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public static UserRole createUserRole(User user) {
        return UserRole.builder()
                .authorityName("ROLE_USER")
                .user(user)
                .build();
    }
}
