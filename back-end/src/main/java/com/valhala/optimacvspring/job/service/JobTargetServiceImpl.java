package com.valhala.optimacvspring.job.service;

import com.valhala.optimacvspring.analysis.api.AnalysisModuleApi;
import com.valhala.optimacvspring.common.exception.ResourceNotFoundException;
import com.valhala.optimacvspring.iam.api.IamApi;
import com.valhala.optimacvspring.job.dto.DashboardOverviewResponse;
import com.valhala.optimacvspring.job.dto.JobRequestDTO;
import com.valhala.optimacvspring.job.JobResponseDTO;
import com.valhala.optimacvspring.job.entities.JobTarget;
import com.valhala.optimacvspring.job.mapper.JobMapper;
import com.valhala.optimacvspring.job.repository.JobTargetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<JobResponseDTO> getMyJobs(String email,String keyword, Pageable pageable) {
        UUID userId = iamApi.findUserIdByEmail(email);
        log.info("user id {}", userId);
        
        if(keyword != null && !keyword.trim().isEmpty()){
            return jobTargetRepository.findAllByUserIdAndTitleContainingIgnoreCase(userId, keyword, pageable)
                    .map(jobMapper::toResponseDTO);
        }
        
        return jobTargetRepository.findAllByUserId(userId, pageable)
                .map(jobMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobResponseDTO> getAllJobsForUser(UUID userId, Pageable pageable) {
        return jobTargetRepository.findAllByUserId(userId, pageable)
                .map(jobMapper::toResponseDTO);
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
        
        // Use MapStruct for robust reactive entity updates from DTO
        jobMapper.updateJobTargetFromDto(dto, jobTarget);

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

    @Override
    public void verifyJobOwnership(UUID jobId, UUID userId) {
        findAndVerifyOwnership(jobId, userId);
    }

    private JobTarget findAndVerifyOwnership(UUID jobId, UUID userId) {
        JobTarget jobTarget = jobTargetRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        if (!jobTarget.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not own this job target");
        }
        return jobTarget;
    }

    @Override
    @Transactional(readOnly = true)
    public String getJobTitle(UUID jobId) {
        return jobTargetRepository.findById(jobId)
                .map(JobTarget::getTitle)
                .orElse("Unknown Job");
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<UUID> getJobIdsByUserIdAndKeyword(UUID userId, String keyword) {
        return jobTargetRepository.findAllByUserIdAndTitleContainingIgnoreCase(userId, keyword, org.springframework.data.domain.Pageable.unpaged())
                .stream()
                .map(com.valhala.optimacvspring.job.entities.JobTarget::getId)
                .toList();
    }
}

