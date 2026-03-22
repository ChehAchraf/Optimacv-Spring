package com.valhala.optimacvspring.resume.service;

import com.valhala.optimacvspring.resume.api.ResumeApi;
import com.valhala.optimacvspring.resume.api.ResumeTextDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeApiImpl implements ResumeApi {

    private final ResumeService resumeService;

    @Override
    public List<ResumeTextDTO> getResumesText(List<UUID> resumeIds) {
        return resumeService.getResumesText(resumeIds);
    }

    @Override
    public void verifyResumeOwnership(UUID resumeId, UUID userId) {
        resumeService.verifyResumeOwnership(resumeId, userId);
    }

    @Override
    public String getResumeFileName(UUID resumeId) {
        return resumeService.getResumeFileName(resumeId);
    }

    @Override
    public List<UUID> getResumeIdsByUserIdAndKeyword(UUID userId, String keyword) {
        return resumeService.getResumeIdsByUserIdAndKeyword(userId, keyword);
    }
}
