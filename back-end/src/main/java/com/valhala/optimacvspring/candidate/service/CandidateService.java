package com.valhala.optimacvspring.candidate.service;

import com.valhala.optimacvspring.candidate.dto.CandidateResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final JobApi jobApi;
    private final ObjectMapper objectMapper;

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

    @Transactional(readOnly = true)
    public Page<CandidateResponseDTO> getCandidatesForJob(UUID companyId, UUID jobId, org.springframework.data.domain.Pageable pageable) {
        jobApi.verifyJobOwnership(jobId, companyId);
        
        org.springframework.data.domain.Page<Candidate> candidates = candidateRepository.findCandidatesByCompanyIdAndJobId(companyId, jobId, pageable);
        
        return candidates.map(c -> new com.valhala.optimacvspring.candidate.dto.CandidateResponseDTO(
                c.getId(),
                c.getOriginalFileName(),
                c.getMatchScore(),
                c.getAiFeedback(),
                c.getUploadedAt()
        ));
    }

    @Transactional(readOnly = true)
    public com.valhala.optimacvspring.candidate.dto.CandidateResponseDTO getCandidateByIdForCompany(UUID companyId, UUID candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));
        
        jobApi.verifyJobOwnership(candidate.getJobId(), companyId);
        
        return new com.valhala.optimacvspring.candidate.dto.CandidateResponseDTO(
                candidate.getId(),
                candidate.getOriginalFileName(),
                candidate.getMatchScore(),
                candidate.getAiFeedback(),
                candidate.getUploadedAt()
        );
    }

    @Transactional
    public void deleteCandidateForCompany(UUID companyId, UUID candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));
                
        // ensure the company owns the job associated with this candidate
        jobApi.verifyJobOwnership(candidate.getJobId(), companyId);
        
        candidateRepository.delete(candidate);
        log.info("Candidate {} deleted successfully by company {}", candidateId, companyId);
    }

}
