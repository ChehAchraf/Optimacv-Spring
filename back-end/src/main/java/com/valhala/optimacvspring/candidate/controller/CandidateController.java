package com.valhala.optimacvspring.candidate.controller;

import com.valhala.optimacvspring.candidate.dto.CandidateResponseDTO;
import com.valhala.optimacvspring.candidate.service.CandidateService;
import com.valhala.optimacvspring.iam.api.IamApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;
    private final IamApi iamApi;

    @PreAuthorize("hasRole('COMPANY')")
    @PostMapping("/jobs/{jobId}/bulk-upload")
    public ResponseEntity<Void> bulkUploadCandidates(
            @PathVariable UUID jobId,
            @RequestParam("files") List<MultipartFile> files,
            @AuthenticationPrincipal UserDetails userDetails) {
            
        UUID companyId = iamApi.findUserIdByEmail(userDetails.getUsername());
        
        candidateService.bulkUploadCandidates(companyId, jobId, files);
        
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasRole('COMPANY')")
    @GetMapping("/jobs/{jobId}/candidates")
    public ResponseEntity<Page<CandidateResponseDTO>> getJobCandidates(
            @PathVariable UUID jobId,
            @org.springframework.data.web.PageableDefault(size = 10) org.springframework.data.domain.Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
            
        UUID companyId = iamApi.findUserIdByEmail(userDetails.getUsername());
        org.springframework.data.domain.Page<CandidateResponseDTO> candidates = candidateService.getCandidatesForJob(companyId, jobId, pageable);
        
        return ResponseEntity.ok(candidates);
    }

    @PreAuthorize("hasRole('COMPANY')")
    @GetMapping("/candidates/{candidateId}")
    public ResponseEntity<CandidateResponseDTO> getCandidateDetails(
            @PathVariable UUID candidateId,
            @AuthenticationPrincipal UserDetails userDetails) {
            
        UUID companyId = iamApi.findUserIdByEmail(userDetails.getUsername());
        com.valhala.optimacvspring.candidate.dto.CandidateResponseDTO candidate = candidateService.getCandidateByIdForCompany(companyId, candidateId);
        
        return ResponseEntity.ok(candidate);
    }

    @PreAuthorize("hasRole('COMPANY')")
    @DeleteMapping("/candidates/{candidateId}")
    public ResponseEntity<Void> deleteCandidate(
            @PathVariable UUID candidateId,
            @AuthenticationPrincipal UserDetails userDetails) {
            
        UUID companyId = iamApi.findUserIdByEmail(userDetails.getUsername());
        candidateService.deleteCandidateForCompany(companyId, candidateId);
        
        return ResponseEntity.noContent().build();
    }
}
