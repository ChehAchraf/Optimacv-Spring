package com.valhala.optimacvspring.analysis.controller;


import com.valhala.optimacvspring.analysis.dto.CvAnalysisResponseDTO;
import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import com.valhala.optimacvspring.analysis.mapper.CvAnalysisMapper;
import com.valhala.optimacvspring.analysis.service.CvAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
