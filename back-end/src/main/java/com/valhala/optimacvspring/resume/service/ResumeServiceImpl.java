package com.valhala.optimacvspring.resume.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.valhala.optimacvspring.common.exception.ResourceNotFoundException;
import com.valhala.optimacvspring.resume.api.ResumeTextDTO;
import com.valhala.optimacvspring.resume.dto.ResumeResponseDTO;
import com.valhala.optimacvspring.resume.entites.Resume;
import com.valhala.optimacvspring.resume.events.CvUploadedEvent;
import com.valhala.optimacvspring.resume.mapper.ResumeMapper;
import com.valhala.optimacvspring.resume.repository.ResumeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final FileStorageService fileStorageService;
    private final ResumeMapper resumeMapper;

    @Override
    @Transactional
    public Resume processAndSaveCv(MultipartFile file, UUID userId, UUID jobId) throws IOException {

        String fileUrl = fileStorageService.storeFile(file, userId);

        ByteArrayResource resource = new ByteArrayResource(file.getBytes());
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource);
        List<Document> documents = pdfReader.get();

        StringBuilder extractedText = new StringBuilder();
        for (Document doc : documents) {
            extractedText.append(doc.getText()).append("\n");
        }
        Resume resume = Resume.builder()
                .fileName(file.getOriginalFilename())
                .fileUrl(fileUrl)
                .userId(userId)
                .extractedText(extractedText.toString())
                .build();

        Resume savedResume = resumeRepository.saveAndFlush(resume);

        eventPublisher.publishEvent(new CvUploadedEvent(savedResume.getId(), extractedText.toString(), jobId));

        return savedResume;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeResponseDTO> getAllResumesForUser(UUID userId) {
        return resumeRepository.findAllByUserId(userId).stream()
                .map(resumeMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public ResumeResponseDTO updateResume(UUID resumeId, UUID jobId, UUID userId) {
        Resume resume = findAndVerifyOwnership(resumeId, userId);

        if (jobId != null) {
            eventPublisher.publishEvent(new CvUploadedEvent(resumeId, resume.getExtractedText(), jobId));
        }

        Resume saved = resumeRepository.save(resume);
        log.info("Resume updated: {} by user: {}", resumeId, userId);
        return resumeMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteResume(UUID resumeId, UUID userId) {
        Resume resume = findAndVerifyOwnership(resumeId, userId);
        resumeRepository.delete(resume);
        log.info("Resume soft-deleted: {} by user: {}", resumeId, userId);
    }

    @Override
    public List<ResumeTextDTO> getResumesText(List<UUID> resumeIds) {
        List<Resume> resumes = resumeRepository.findAllByIdIn(resumeIds);

        return resumes.stream()
                .map(resume -> new ResumeTextDTO(resume.getId(), resume.getExtractedText()))
                .toList();
    }

    @Override
    public void verifyResumeOwnership(UUID resumeId, UUID userId) {
        findAndVerifyOwnership(resumeId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public String getResumeFileName(UUID resumeId) {
        return resumeRepository.findById(resumeId)
                .map(Resume::getFileName)
                .orElse("Unknown Resume");
    }

    private Resume findAndVerifyOwnership(UUID resumeId, UUID userId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with id: " + resumeId));
        if (!resume.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not own this resume");
        }
        return resume;
    }
}
