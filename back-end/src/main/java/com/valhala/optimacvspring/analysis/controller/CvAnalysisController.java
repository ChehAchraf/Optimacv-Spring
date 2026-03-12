package com.valhala.optimacvspring.analysis.controller;


import com.valhala.optimacvspring.analysis.dto.BulkRankingRequestDTO;
import com.valhala.optimacvspring.analysis.dto.CvAnalysisResponseDTO;
import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import com.valhala.optimacvspring.analysis.mapper.CvAnalysisMapper;
import com.valhala.optimacvspring.analysis.service.CvAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor

public class CvAnalysisController {

    private final CvAnalysisService analysisService;
    private final CvAnalysisMapper analysisMapper;

    @GetMapping("/{resumeId}")
    public ResponseEntity<CvAnalysisResponseDTO> getAnalysisResult(@PathVariable UUID resumeId) {

        CvAnalysisResult result = analysisService.getAnalysisByResumeId(resumeId);

        return ResponseEntity.ok(analysisMapper.toResponseDTO(result));

    }

    @PreAuthorize("hasRole('COMPANY')")
    @PostMapping("/bulk-rank")
    public ResponseEntity<String> bulkRankResumes(@RequestBody BulkRankingRequestDTO request) {

        String rankingResult = analysisService.rankMultipleResumes(request);

        return ResponseEntity.ok(rankingResult);
    }

}
