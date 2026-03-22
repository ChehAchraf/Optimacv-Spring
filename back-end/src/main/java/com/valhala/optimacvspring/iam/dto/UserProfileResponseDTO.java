package com.valhala.optimacvspring.iam.dto;

import java.util.UUID;

public record UserProfileResponseDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String role
) {
}
