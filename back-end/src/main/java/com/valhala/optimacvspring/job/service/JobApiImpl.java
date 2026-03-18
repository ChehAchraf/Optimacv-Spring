package com.valhala.optimacvspring.job.service;

import com.valhala.optimacvspring.job.api.JobApi;
import com.valhala.optimacvspring.job.JobResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobApiImpl implements JobApi {

    private final JobTargetService jobTargetService;

    @Override
    public JobResponseDTO getJobDetails(UUID jobId) {
        return jobTargetService.getJobDetails(jobId);
    }

    @Override
    public void verifyJobOwnership(UUID jobId, UUID userId) {
        jobTargetService.verifyJobOwnership(jobId, userId);
    }

    @Override
    public String getJobTitle(UUID jobId) {
        return jobTargetService.getJobTitle(jobId);
    }
}

