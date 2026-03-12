package com.valhala.optimacvspring.analysis.dto;

import java.util.List;
import java.util.UUID;

public record BulkRankingRequestDTO(
        UUID jobId,
        List<UUID> resumeIds
) {
}
