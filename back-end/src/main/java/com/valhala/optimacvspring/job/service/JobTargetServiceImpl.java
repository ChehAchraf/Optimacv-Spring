package com.valhala.optimacvspring.job.service;

import com.valhala.optimacvspring.analysis.api.AnalysisModuleApi;
import com.valhala.optimacvspring.common.exception.ResourceNotFoundException;
import com.valhala.optimacvspring.iam.api.IamApi;
import com.valhala.optimacvspring.job.dto.DashboardOverviewResponse;
import com.valhala.optimacvspring.job.dto.JobRequestDTO;
import com.valhala.optimacvspring.job.dto.JobResponseDTO;
import com.valhala.optimacvspring.job.entities.JobTarget;
import com.valhala.optimacvspring.job.mapper.JobMapper;
import com.valhala.optimacvspring.job.repository.JobTargetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobTargetServiceImpl implements JobTargetService {

    private final JobTargetRepository jobTargetRepository;
    private final JobMapper jobMapper;
    private final IamApi iamApi;
    private final AnalysisModuleApi analysisModuleApi;

    @Override
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
    public List<JobResponseDTO> getMyJobs(String email) {
        UUID userId = iamApi.findUserIdByEmail(email);
        log.info("user id {}", userId);
        return jobTargetRepository.findAllByUserId(userId).stream()
                .map(jobMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDTO> getAllJobsForUser(UUID userId) {
        return jobTargetRepository.findAllByUserId(userId).stream()
                .map(jobMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardOverviewResponse getDashboardOverview(String userEmail) {
        UUID userId = iamApi.findUserIdByEmail(userEmail);

        long totalTargets = jobTargetRepository.countByUserId(userId);

        List<DashboardOverviewResponse.RecentJobTarget> recentJobs = jobTargetRepository.findTop3ByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(job -> new DashboardOverviewResponse.RecentJobTarget(
                        job.getId(),
                        job.getTitle(),
                        job.getCompany(),
                        job.getCreatedAt()
                ))
                .toList();

        long totalAnalyses = analysisModuleApi.getTotalAnalysesByUser(userId);
        double avgScore = analysisModuleApi.getAverageScoreByUser(userId);

        return new DashboardOverviewResponse(totalTargets, totalAnalyses, avgScore, recentJobs);
    }

    @Override
    @Transactional
    public JobResponseDTO updateJob(UUID jobId, JobRequestDTO dto, UUID userId) {
        JobTarget jobTarget = findAndVerifyOwnership(jobId, userId);
        jobTarget.setTitle(dto.title());
        jobTarget.setCompany(dto.company());
        jobTarget.setDescription(dto.description());

        JobTarget saved = jobTargetRepository.save(jobTarget);
        log.info("Job target updated: {} by user: {}", jobId, userId);
        return jobMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteJob(UUID jobId, UUID userId) {
        JobTarget jobTarget = findAndVerifyOwnership(jobId, userId);
        jobTargetRepository.delete(jobTarget);
        log.info("Job target soft-deleted: {} by user: {}", jobId, userId);
    }

    @Override
    public boolean checkIfJobExists(UUID jobId) {
        log.info("finding job with id : {} ", jobId);
        return this.jobTargetRepository.existsById(jobId);
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponseDTO getJobDetails(UUID jobId) {
        JobTarget jobTarget = jobTargetRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        return jobMapper.toResponseDTO(jobTarget);
    }

    private JobTarget findAndVerifyOwnership(UUID jobId, UUID userId) {
        JobTarget jobTarget = jobTargetRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        if (!jobTarget.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not own this job target");
        }
        return jobTarget;
    }
}

