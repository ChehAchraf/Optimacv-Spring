package com.valhala.optimacvspring.analysis.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CvAnalysisHistoryDTO(
        UUID analysisId,
        LocalDateTime analyzedAt,
        String resumeFileName,
        String jobTitle,
        String feedback
) {
}
