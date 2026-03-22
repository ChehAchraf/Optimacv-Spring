package com.valhala.optimacvspring.job.service;

import com.valhala.optimacvspring.job.dto.DashboardOverviewResponse;
import com.valhala.optimacvspring.job.dto.JobRequestDTO;
import com.valhala.optimacvspring.job.JobResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface JobTargetService {

    JobResponseDTO createJob(JobRequestDTO request, UUID userId);

    Page<JobResponseDTO> getMyJobs(String email, String keyword, Pageable pageable);

    Page<JobResponseDTO> getAllJobsForUser(UUID userId, Pageable pageable);

    DashboardOverviewResponse getDashboardOverview(String userEmail);

    JobResponseDTO updateJob(UUID jobId, JobRequestDTO dto, UUID userId);

    void deleteJob(UUID jobId, UUID userId);

    boolean checkIfJobExists(UUID jobId);

    JobResponseDTO getJobDetails(UUID jobId);

    void verifyJobOwnership(UUID jobId, UUID userId);

    String getJobTitle(UUID jobId);

    java.util.List<UUID> getJobIdsByUserIdAndKeyword(UUID userId, String keyword);
}
