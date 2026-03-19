package com.valhala.optimacvspring.analysis.service;

import com.valhala.optimacvspring.analysis.api.AnalysisModuleApi;
import com.valhala.optimacvspring.analysis.repository.CvAnalysisResultRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class AnalysisModuleApiImpl implements AnalysisModuleApi {

    private final CvAnalysisResultRepository analysisRepository;

    @Override
    public long getTotalAnalysesByUser(UUID userId) {
        return analysisRepository.countByUserId(userId);
    }

    @Override
    public double getAverageScoreByUser(UUID userId) {
        return analysisRepository.findAverageScoreByUserId(userId);
    }

}
