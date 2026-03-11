package com.valhala.optimacvspring.iam.dto;

public record AuthResponseDTO(
        String token,
        String email,
        String role
) {
}
