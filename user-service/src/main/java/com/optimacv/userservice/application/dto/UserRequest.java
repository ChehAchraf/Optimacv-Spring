package com.optimacv.userservice.application.dto;

import com.optimacv.userservice.domain.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email @NotBlank
        String email,
        @NotBlank
        String password,
        Role role
) {
}
