package com.valhala.optimacvspring.analysis.service;

import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import com.valhala.optimacvspring.analysis.repository.CvAnalysisResultRepository;
import com.valhala.optimacvspring.resume.events.CvUploadedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CvAnalysisService {

    private final ChatClient chatClient;
    private final CvAnalysisResultRepository repository;

    public CvAnalysisService(ChatClient.Builder chatClientBuilder, CvAnalysisResultRepository repository) {
        this.chatClient = chatClientBuilder.build();
        this.repository = repository;
    }

    @ApplicationModuleListener
    public void onCvUploaded(CvUploadedEvent event) {
        log.info("Received event for Resume ID: {}", event.resumeId());

        try {
            String analysisResult = analyzeCv(event.extractedText());

            CvAnalysisResult result = CvAnalysisResult.builder()
                    .resumeId(event.resumeId())
                    .feedback(analysisResult)
                    .build();

            repository.save(result);

            log.info("AI Analysis saved successfully for Resume ID: {}", event.resumeId());

        } catch (Exception e) {
            log.error("Failed to analyze CV for Resume ID: {}", event.resumeId(), e);
        }
    }

    public String analyzeCv(String cvText) {
        String systemPrompt = """
            You are an elite Tech Recruiter and a Principal Software Engineer with years of experience hiring top-tier talent.
            Your task is to conduct a rigorous, professional analysis of the provided resume (CV) text.
            
            Please provide your analysis strictly in the following structured format, using Markdown:
            
            ### 1. Overall Score & Verdict
            Provide a realistic score out of 10 (e.g., 7.5/10) based on impact, clarity, and ATS readability. Add a brief 2-sentence summary of your verdict.
            
            ### 2. Key Strengths
            Highlight what the candidate did well (e.g., quantifiable achievements, strong tech stack presentation, clear project descriptions).
            
            ### 3. Areas of Weakness & Missing Elements
            Point out what is holding this CV back (e.g., vague bullet points, missing portfolio/GitHub links, passive language, poor structure). Be specific and constructive.
            
            ### 4. ATS Optimization & Actionable Tips
            Provide 3 to 5 concrete, actionable steps the candidate must take to improve this CV for Applicant Tracking Systems (ATS) and human recruiters. Mention specific keyword placement or formatting tweaks.
            
            Maintain a professional, objective, and highly analytical tone. Do not use generic filler words; give highly specific advice based on the provided text.
            """;

        return chatClient.prompt()
                .system(systemPrompt)
                .user(cvText)
                .call()
                .content();
    }

    public CvAnalysisResult getAnalysisByResumeId(UUID resumeId) {
        return repository.findByResumeId(resumeId)
                .orElseThrow(() -> new RuntimeException("التحليل غير موجود أو مازال قيد المعالجة للـ CV رقم: " + resumeId));
    }

}