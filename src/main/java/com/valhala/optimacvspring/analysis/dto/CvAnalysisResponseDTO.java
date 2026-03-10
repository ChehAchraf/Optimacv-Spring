package com.valhala.optimacvspring.analysis.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CvAnalysisResponseDTO(
        UUID id,
        UUID resumeId,
        String feedback,
        LocalDateTime analyzedAt
) {
}
