package com.valhala.optimacvspring.analysis.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CvAnalysisResponseDTO(
        UUID id,
        String resumeName,
        String jobTitle,
        UUID resumeId,
        String feedback,
        LocalDateTime analyzedAt
) {
}
