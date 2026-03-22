package com.valhala.optimacvspring.iam.dto;

public record AuthResponseDTO(
        String token,
        String email,
        String firstName,
        String lastName,
        String role
) {
}
