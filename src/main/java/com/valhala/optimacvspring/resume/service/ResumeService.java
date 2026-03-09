package com.valhala.optimacvspring.resume.service;

import com.valhala.optimacvspring.resume.entites.Resume;
import com.valhala.optimacvspring.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.ai.document.Document;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public String processAndSaveCv(MultipartFile file, UUID userId) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(file.getBytes());
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource);
        List<Document> documents = pdfReader.get();

        StringBuilder extractedText = new StringBuilder();
        for (Document doc : documents) {
            extractedText.append(doc.getText()).append("\n");
        }
        Resume resume = Resume.builder()
                .fileName(file.getOriginalFilename())
                .fileUrl("temporarily-not-saved-locally")
                .userId(userId)
                .build();

        resumeRepository.save(resume);
        return extractedText.toString();
    }

}
