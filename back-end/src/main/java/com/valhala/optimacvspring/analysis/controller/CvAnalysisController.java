package com.valhala.optimacvspring.analysis.controller;


import com.valhala.optimacvspring.analysis.dto.BulkRankingRequestDTO;
import com.valhala.optimacvspring.analysis.dto.CvAnalysisRequestDTO;
import com.valhala.optimacvspring.analysis.dto.CvAnalysisResponseDTO;
import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import com.valhala.optimacvspring.analysis.mapper.CvAnalysisMapper;
import com.valhala.optimacvspring.analysis.service.CvAnalysisService;
import com.valhala.optimacvspring.iam.api.IamApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor

public class CvAnalysisController {

    private final CvAnalysisService analysisService;
    private final CvAnalysisMapper analysisMapper;
    private final IamApi iamApi;

    @PostMapping("/start")
    public ResponseEntity<CvAnalysisResponseDTO> startAnalysis(
            @RequestBody CvAnalysisRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = iamApi.findUserIdByEmail(userDetails.getUsername());
        CvAnalysisResult result = analysisService.startAnalysis(request.resumeId(), request.jobId(), userId);
        return ResponseEntity.ok(analysisMapper.toResponseDTO(result));
    }

    @GetMapping("/{analysisId}")
    public ResponseEntity<CvAnalysisResponseDTO> getAnalysisResult(
            @PathVariable UUID analysisId,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = iamApi.findUserIdByEmail(userDetails.getUsername());
        CvAnalysisResult result = analysisService.getAnalysisById(analysisId, userId);

        return ResponseEntity.ok(analysisMapper.toResponseDTO(result));
    }

    @GetMapping("/history")
    public ResponseEntity<org.springframework.data.domain.Page<com.valhala.optimacvspring.analysis.dto.CvAnalysisHistoryDTO>> getAnalysisHistory(
            @org.springframework.data.web.PageableDefault(size = 10, sort = "analyzedAt", direction = org.springframework.data.domain.Sort.Direction.DESC) org.springframework.data.domain.Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        UUID userId = iamApi.findUserIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(analysisService.getAnalysisHistory(userId, pageable));
    }

    @PreAuthorize("hasRole('COMPANY')")
    @PostMapping("/bulk-rank")
    public ResponseEntity<String> bulkRankResumes(@RequestBody BulkRankingRequestDTO request) {

        String rankingResult = analysisService.rankMultipleResumes(request);

        return ResponseEntity.ok(rankingResult);
    }

}
