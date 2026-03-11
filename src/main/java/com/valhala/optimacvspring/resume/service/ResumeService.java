package com.valhala.optimacvspring.resume.service;

import com.valhala.optimacvspring.resume.entites.Resume;
import com.valhala.optimacvspring.resume.events.CvUploadedEvent;
import com.valhala.optimacvspring.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.ai.document.Document;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final FileStorageService fileStorageService;

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
                .jobId(jobId)
                .extractedText(extractedText.toString())
                .build();

        Resume savedResume = resumeRepository.saveAndFlush(resume);

        eventPublisher.publishEvent(new CvUploadedEvent(resume.getId(), extractedText.toString(), jobId));

        return savedResume;
    }
}
