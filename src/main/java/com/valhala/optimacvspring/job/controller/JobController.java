package com.valhala.optimacvspring.job.controller;

import com.valhala.optimacvspring.iam.api.IamApi;
import com.valhala.optimacvspring.job.dto.JobRequestDTO;
import com.valhala.optimacvspring.job.dto.JobResponseDTO;
import com.valhala.optimacvspring.job.service.JobTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobTargetService jobTargetService;
    private final IamApi iamApi;

    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(
            @RequestBody JobRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = iamApi.findUserIdByEmail(userDetails.getUsername());
        JobResponseDTO response = jobTargetService.createJob(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
