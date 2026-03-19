package com.valhala.optimacvspring.resume.service;

import com.valhala.optimacvspring.resume.dto.ResumeResponseDTO;
import com.valhala.optimacvspring.resume.entites.Resume;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.valhala.optimacvspring.resume.api.ResumeTextDTO;

public interface ResumeService {

    Resume processAndSaveCv(MultipartFile file, UUID userId, UUID jobId) throws IOException;

    Page<ResumeResponseDTO> getAllResumesForUser(UUID userId, Pageable pageable);

    ResumeResponseDTO updateResume(UUID resumeId, UUID jobId, UUID userId);

    void deleteResume(UUID resumeId, UUID userId);

    List<ResumeTextDTO> getResumesText(List<UUID> resumeIds);

    void verifyResumeOwnership(UUID resumeId, UUID userId);

    String getResumeFileName(UUID resumeId);
}
