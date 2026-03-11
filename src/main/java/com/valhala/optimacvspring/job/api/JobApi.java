package com.valhala.optimacvspring.job.api;

import com.valhala.optimacvspring.job.dto.JobResponseDTO;

import java.util.UUID;

public interface JobApi {

    JobResponseDTO getJobDetails(UUID jobId);
}
