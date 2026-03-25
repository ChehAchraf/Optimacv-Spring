package com.valhala.optimacvspring.analysis.service;

import com.valhala.optimacvspring.analysis.dto.BulkRankingRequestDTO;
import com.valhala.optimacvspring.analysis.dto.CvAnalysisHistoryDTO;
import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import com.valhala.optimacvspring.job.JobResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CvAnalysisService {

    String analyzeCv(String cvText, JobResponseDTO job);

    String analyzeTargetedCv(String cvText, JobResponseDTO job);

    CvAnalysisResult getAnalysisById(UUID analysisId, UUID userId);

    void deleteAnalysis(UUID analysisId, UUID userId);

    Page<CvAnalysisHistoryDTO> getAnalysisHistory(UUID userId, String keyword, Pageable pageable);

    String rankMultipleResumes(BulkRankingRequestDTO request);

    CvAnalysisResult startAnalysis(UUID resumeId, UUID jobId, UUID userId);
}