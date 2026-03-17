package com.valhala.optimacvspring.job.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DashboardOverviewResponse(
        long totalTargets,
        long analysesDone,
        double averageScore,
        List<RecentJobTarget> recentTargets
) {
    public record RecentJobTarget(
            UUID id,
            String title,
            String company,
            LocalDateTime createdAt
    ) {}
}