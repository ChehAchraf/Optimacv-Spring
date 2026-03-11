package com.valhala.optimacvspring.job.service;

import com.valhala.optimacvspring.common.exception.ResourceNotFoundException;
import com.valhala.optimacvspring.job.api.JobApi;
import com.valhala.optimacvspring.job.dto.JobRequestDTO;
import com.valhala.optimacvspring.job.dto.JobResponseDTO;
import com.valhala.optimacvspring.job.entities.JobTarget;
import com.valhala.optimacvspring.job.mapper.JobMapper;
import com.valhala.optimacvspring.job.repository.JobTargetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobTargetService implements JobApi {

    private final JobTargetRepository jobTargetRepository;
    private final JobMapper jobMapper;

    @Transactional
    public JobResponseDTO createJob(JobRequestDTO request, UUID userId) {
        JobTarget jobTarget = JobTarget.builder()
                .userId(userId)
                .title(request.title())
                .company(request.company())
                .description(request.description())
                .build();

        JobTarget saved = jobTargetRepository.save(jobTarget);
        log.info("Job target created: {} for user: {}", saved.getId(), userId);
        return jobMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponseDTO getJobDetails(UUID jobId) {
        JobTarget jobTarget = jobTargetRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        return jobMapper.toResponseDTO(jobTarget);
    }
}
