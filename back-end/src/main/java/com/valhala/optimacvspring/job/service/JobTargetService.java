package com.valhala.optimacvspring.job.service;

import com.valhala.optimacvspring.job.dto.DashboardOverviewResponse;
import com.valhala.optimacvspring.job.dto.JobRequestDTO;
import com.valhala.optimacvspring.job.JobResponseDTO;

import java.util.List;

import java.util.List;
import java.util.UUID;

public interface JobTargetService {

    JobResponseDTO createJob(JobRequestDTO request, UUID userId);

    List<JobResponseDTO> getMyJobs(String email);

    List<JobResponseDTO> getAllJobsForUser(UUID userId);

    DashboardOverviewResponse getDashboardOverview(String userEmail);

    JobResponseDTO updateJob(UUID jobId, JobRequestDTO dto, UUID userId);

    void deleteJob(UUID jobId, UUID userId);

    boolean checkIfJobExists(UUID jobId);

    JobResponseDTO getJobDetails(UUID jobId);

    void verifyJobOwnership(UUID jobId, UUID userId);

    String getJobTitle(UUID jobId);
}
