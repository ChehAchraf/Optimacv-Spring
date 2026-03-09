package com.valhala.optimacvspring.resume.controller;


import com.valhala.optimacvspring.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") UUID userId) {

        try {
            String extractedText = resumeService.processAndSaveCv(file, userId);

            return ResponseEntity.ok(extractedText);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("وقع شي بلان فاش كنا كنقراو الـ CV: " + e.getMessage());
        }
    }
}
