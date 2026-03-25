package com.valhala.optimacvspring.candidate.service;

import com.valhala.optimacvspring.candidate.entities.Candidate;
import com.valhala.optimacvspring.candidate.events.CandidateUploadedEvent;
import com.valhala.optimacvspring.candidate.repository.CandidateRepository;
import com.valhala.optimacvspring.job.api.JobApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final JobApi jobApi;

    @Transactional
    public void bulkUploadCandidates(UUID companyId, UUID jobId, List<MultipartFile> files) {
        jobApi.verifyJobOwnership(jobId, companyId);
        log.info("Starting bulk upload for job {} by company {}. Total files: {}", jobId, companyId, files.size());

        for (MultipartFile file : files) {
            try {
                processAndSaveSingleCandidate(companyId, jobId, file);
            } catch (Exception e) {
                log.error("Failed to process file {}: {}", file.getOriginalFilename(), e.getMessage());
            }
        }
    }

    private void processAndSaveSingleCandidate(UUID companyId, UUID jobId, MultipartFile file) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(file.getBytes());
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource);
        List<Document> documents = pdfReader.get();

        StringBuilder extractedText = new StringBuilder();
        for (Document doc : documents) {
            extractedText.append(doc.getText()).append("\n");
        }

        Candidate candidate = Candidate.builder()
                .companyId(companyId)
                .jobId(jobId)
                .originalFileName(file.getOriginalFilename())
                .parsedCvText(extractedText.toString())
                .build();

        Candidate saved = candidateRepository.save(candidate);
        
        eventPublisher.publishEvent(new CandidateUploadedEvent(saved.getId(), extractedText.toString(), jobId));
        log.info("Candidate {} saved successfully and event published for AI ranking", saved.getId());
    }
}
