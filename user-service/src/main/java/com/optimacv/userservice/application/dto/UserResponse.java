package com.optimacv.userservice.application.dto;

import com.optimacv.userservice.domain.model.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        LocalDateTime createdAt
) {
}
