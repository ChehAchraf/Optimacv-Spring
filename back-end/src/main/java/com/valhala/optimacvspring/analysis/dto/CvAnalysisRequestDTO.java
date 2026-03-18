package com.valhala.optimacvspring.analysis.dto;

import java.util.UUID;

public record CvAnalysisRequestDTO(
        UUID resumeId,
        UUID jobId
) {
}
