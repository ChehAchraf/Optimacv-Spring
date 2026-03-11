package com.valhala.optimacvspring.resume.controller;


import com.valhala.optimacvspring.iam.api.IamApi;
import com.valhala.optimacvspring.resume.dto.ResumeResponseDTO;
import com.valhala.optimacvspring.resume.entites.Resume;
import com.valhala.optimacvspring.resume.mapper.ResumeMapper;
import com.valhala.optimacvspring.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeMapper resumeMapper;
    private final IamApi iamApi;

    @PostMapping("/upload")
    public ResponseEntity<ResumeResponseDTO> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "jobId", required = false) UUID jobId,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            UUID userId = iamApi.findUserIdByEmail(userDetails.getUsername());
            Resume savedResume = resumeService.processAndSaveCv(file, userId, jobId);

            ResumeResponseDTO response = resumeMapper.toResponseDTO(savedResume);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("وقع شي بلان: " + e.getMessage());
        }
    }
}
