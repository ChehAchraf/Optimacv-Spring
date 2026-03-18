package com.valhala.optimacvspring.job.api;

import com.valhala.optimacvspring.job.JobResponseDTO;

import java.util.UUID;

public interface JobApi {

    JobResponseDTO getJobDetails(UUID jobId);

    void verifyJobOwnership(UUID jobId, UUID userId);

    String getJobTitle(UUID jobId);
}
