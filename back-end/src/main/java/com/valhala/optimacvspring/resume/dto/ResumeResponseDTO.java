package com.valhala.optimacvspring.resume.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResumeResponseDTO(
        UUID id,
        String fileName,
        LocalDateTime uploadedAt,
        String statusMessage
) {
}
