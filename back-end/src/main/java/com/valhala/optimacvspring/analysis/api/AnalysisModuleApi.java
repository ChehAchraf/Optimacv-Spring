package com.valhala.optimacvspring.analysis.api;

import java.util.UUID;

public interface AnalysisModuleApi {
    long getTotalAnalysesByUser(UUID userId);
    double getAverageScoreByUser(UUID userId);
}
