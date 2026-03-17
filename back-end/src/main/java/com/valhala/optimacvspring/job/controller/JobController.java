package com.valhala.optimacvspring.job.controller;

import com.valhala.optimacvspring.iam.api.IamApi;
import com.valhala.optimacvspring.iam.service.CustomUserDetailsService;
import com.valhala.optimacvspring.job.dto.DashboardOverviewResponse;
import com.valhala.optimacvspring.job.dto.JobRequestDTO;
import com.valhala.optimacvspring.job.dto.JobResponseDTO;
import com.valhala.optimacvspring.job.service.JobTargetService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
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

    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getAllJobs(
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = iamApi.findUserIdByEmail(userDetails.getUsername());
        List<JobResponseDTO> jobs = jobTargetService.getAllJobsForUser(userId);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/my-jobs")
    public ResponseEntity<List<JobResponseDTO>> getMyJobs(Principal principal ){

        String userEmail = principal.getName();


        List<JobResponseDTO> myJobs = jobTargetService.getMyJobs(userEmail);

        return ResponseEntity.ok(myJobs);
    }

    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewResponse> getOverview(
            @Parameter(hidden = true) Principal principal) {

        DashboardOverviewResponse overview = jobTargetService.getDashboardOverview(principal.getName());
        return ResponseEntity.ok(overview);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponseDTO> updateJob(
            @PathVariable UUID jobId,
            @RequestBody JobRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = iamApi.findUserIdByEmail(userDetails.getUsername());
        JobResponseDTO response = jobTargetService.updateJob(jobId, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable UUID jobId,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = iamApi.findUserIdByEmail(userDetails.getUsername());
        jobTargetService.deleteJob(jobId, userId);
        return ResponseEntity.noContent().build();
    }
}
