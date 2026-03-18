package com.valhala.optimacvspring.analysis.service;

import com.valhala.optimacvspring.analysis.dto.BulkRankingRequestDTO;
import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import com.valhala.optimacvspring.analysis.repository.CvAnalysisResultRepository;
import com.valhala.optimacvspring.job.api.JobApi;
import com.valhala.optimacvspring.job.JobResponseDTO;
import com.valhala.optimacvspring.resume.api.ResumeApi;
import com.valhala.optimacvspring.resume.api.ResumeTextDTO;
import com.valhala.optimacvspring.resume.events.CvUploadedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CvAnalysisService  {

    private final ChatClient chatClient;
    private final CvAnalysisResultRepository repository;
    private final JobApi jobApi;
    private final ResumeApi resumeApi;

    public CvAnalysisService(ChatClient.Builder chatClientBuilder, CvAnalysisResultRepository repository, JobApi jobApi, ResumeApi resumeApi) {
        this.chatClient = chatClientBuilder.build();
        this.repository = repository;
        this.jobApi = jobApi;
        this.resumeApi = resumeApi;
    }

    @ApplicationModuleListener
    public void onCvUploaded(CvUploadedEvent event) {
        log.info("Received event for Resume ID: {}", event.resumeId());

        try {
            String analysisResult;

            if (event.jobId() != null) {
                log.info("Targeted analysis requested for Job ID: {}", event.jobId());
                JobResponseDTO job = jobApi.getJobDetails(event.jobId());
                analysisResult = analyzeTargetedCv(event.extractedText(), job);
            } else {
                analysisResult = analyzeCv(event.extractedText());
            }

            CvAnalysisResult result = CvAnalysisResult.builder()
                    .resumeId(event.resumeId())
                    .jobId(event.jobId())
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
                You are an expert ATS (Applicant Tracking System) and Technical Recruiter.
                Analyze the provided Resume against the Job Target.
               \s
                CRITICAL INSTRUCTION:
                You MUST return the result EXCLUSIVELY as a valid JSON object. Do not include any Markdown formatting, greetings, or explanations outside the JSON structure.
               \s
                Use exactly this JSON schema:
                {
                  "score": <number between 0 and 100>,
                  "verdict": "<short string summarizing the match>",
                  "matchingSkills": [
                    "<skill 1>",
                    "<skill 2>"
                  ],
                  "missingKeywords": [
                    "<keyword 1>",
                    "<keyword 2>"
                  ],
                  "actionPlan": [
                    {
                      "title": "<short title for the advice>",
                      "description": "<detailed advice on how to fix this>"
                    }
                  ]
                }
           \s""";

        return chatClient.prompt()
                .system(systemPrompt)
                .user(cvText)
                .call()
                .content();
    }

    public String analyzeTargetedCv(String cvText, JobResponseDTO job) {
        String systemPrompt = """
        You are an Elite Tech Recruiter and a state-of-the-art Applicant Tracking System (ATS).
        Analyze the candidate's resume against the specific job posting.

        **Target Job:**
        - Title: %s
        - Company: %s
        - Description: %s

        CRITICAL INSTRUCTION:
        You MUST return the result EXCLUSIVELY as a valid JSON object. 
        Do not include any Markdown formatting, greetings, or explanations.

        Required JSON Structure:
        {
          "score": <number 0-100>,
          "verdict": "<2-sentence summary of the match>",
          "matchingSkills": ["skill1", "skill2"],
          "missingKeywords": ["keyword1", "keyword2"],
          "actionPlan": [
            { "title": "Step title", "description": "Specific advice" }
          ]
        }
        """.formatted(job.title(), job.company(), job.description());

        String response = chatClient.prompt()
                .system(systemPrompt)
                .user(cvText)
                .call()
                .content();

        if (response != null) {
            response = response.trim();
            if (response.startsWith("```json")) {
                response = response.substring(7, response.length() - 3).trim();
            } else if (response.startsWith("```")) {
                response = response.substring(3, response.length() - 3).trim();
            }
        }

        return response;
    }

    public CvAnalysisResult getAnalysisById(UUID analysisId, UUID userId) {
        return repository.findByIdAndUserId(analysisId, userId)
                .orElseThrow(() -> new RuntimeException("Analysis not found or access denied: " + analysisId));
    }

    public org.springframework.data.domain.Page<com.valhala.optimacvspring.analysis.dto.CvAnalysisHistoryDTO> getAnalysisHistory(UUID userId, org.springframework.data.domain.Pageable pageable) {
        return repository.findAllByUserIdOrderByAnalyzedAtDesc(userId, pageable)
                .map(result -> {
                    String resumeName = resumeApi.getResumeFileName(result.getResumeId());
                    String jobTitle = result.getJobId() != null ? jobApi.getJobTitle(result.getJobId()) : "General Analysis";
                    return new com.valhala.optimacvspring.analysis.dto.CvAnalysisHistoryDTO(
                            result.getId(),
                            result.getAnalyzedAt(),
                            resumeName,
                            jobTitle,
                            result.getFeedback()
                    );
                });
    }

    public String rankMultipleResumes(BulkRankingRequestDTO request) {

        var jobDetails = jobApi.getJobDetails(request.jobId());

        List<ResumeTextDTO> resumes = resumeApi.getResumesText(request.resumeIds());

        if (resumes.isEmpty()) {
            throw new IllegalArgumentException("No valid resumes found for the provided IDs.");
        }

        log.info("Starting bulk ranking for Job ID: {} with {} resumes", request.jobId(), resumes.size());

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an Elite Tech Recruiter and an advanced ATS (Applicant Tracking System). ");
        prompt.append("Your task is to evaluate and rank multiple candidates for a specific job based on their resumes.\n\n");

        prompt.append("### JOB DESCRIPTION ###\n");
        prompt.append("Title: ").append(jobDetails.title()).append("\n");
        prompt.append("Company: ").append(jobDetails.company()).append("\n");
        prompt.append("Description: ").append(jobDetails.description()).append("\n\n");

        prompt.append("### CANDIDATES RESUMES ###\n");
        for (ResumeTextDTO cv : resumes) {
            prompt.append("--- CANDIDATE ID: ").append(cv.resumeId()).append(" ---\n");
            prompt.append(cv.extractedText()).append("\n\n");
        }

        prompt.append("### INSTRUCTIONS ###\n");
        prompt.append("Rank the candidates from best match to worst match based on the job description. ");
        prompt.append("You MUST return ONLY a valid JSON array. Do NOT include any markdown formatting like ```json. ");
        prompt.append("The JSON structure MUST exactly match this format:\n");
        prompt.append("[\n");
        prompt.append("  {\n");
        prompt.append("    \"resumeId\": \"<exact_candidate_id_from_above>\",\n");
        prompt.append("    \"candidateName\": \"<extract_the_candidate_full_name_from_the_resume_text>\",\n"); // <--- هاد السطر هو اللي زدنا
        prompt.append("    \"rank\": <integer_rank_starting_from_1>,\n");
        prompt.append("    \"matchScore\": <percentage_number_out_of_100>,\n");
        prompt.append("    \"reason\": \"<brief_explanation_of_why_they_got_this_rank>\",\n");
        prompt.append("    \"missingSkills\": [\"<skill_1>\", \"<skill_2>\"]\n");
        prompt.append("  }\n");
        prompt.append("]");

        String response = chatClient.prompt()
                .user(prompt.toString())
                .call()
                .content();

        if (response != null && response.trim().startsWith("```json")) {
            response = response.replace("```json", "").replace("```", "").trim();
        }

        return response;
    }

    public CvAnalysisResult startAnalysis(UUID resumeId, UUID jobId, UUID userId) {
        resumeApi.verifyResumeOwnership(resumeId, userId);
        jobApi.verifyJobOwnership(jobId, userId);
        List<ResumeTextDTO> resumes = resumeApi.getResumesText(List.of(resumeId));
        if (resumes.isEmpty()) {
            throw new IllegalArgumentException("No resume found with id: " + resumeId);
        }
        ResumeTextDTO resumeText = resumes.get(0);
        JobResponseDTO job = jobApi.getJobDetails(jobId);
        String feedback = analyzeTargetedCv(resumeText.extractedText(), job);
        CvAnalysisResult result = CvAnalysisResult.builder()
                .resumeId(resumeId)
                .jobId(jobId)
                .userId(userId)
                .feedback(feedback)
                .build();
        return repository.save(result);
    }

}