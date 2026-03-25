package com.valhala.optimacvspring.analysis.service;

import com.valhala.optimacvspring.analysis.dto.BulkRankingRequestDTO;
import com.valhala.optimacvspring.analysis.dto.CvAnalysisHistoryDTO;
import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import com.valhala.optimacvspring.analysis.repository.CvAnalysisResultRepository;
import com.valhala.optimacvspring.job.api.JobApi;
import com.valhala.optimacvspring.job.JobResponseDTO;
import com.valhala.optimacvspring.resume.api.ResumeApi;
import com.valhala.optimacvspring.resume.api.ResumeTextDTO;
import com.valhala.optimacvspring.resume.events.CvUploadedEvent;
import com.valhala.optimacvspring.candidate.events.CandidateUploadedEvent;
import com.valhala.optimacvspring.candidate.api.CandidateApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CvAnalysisServiceImpl implements CvAnalysisService {

    private final ChatClient chatClient;
    private final CvAnalysisResultRepository repository;
    private final JobApi jobApi;
    private final ResumeApi resumeApi;
    private final CandidateApi candidateApi;

    public CvAnalysisServiceImpl(ChatClient.Builder chatClientBuilder, CvAnalysisResultRepository repository, JobApi jobApi, ResumeApi resumeApi, CandidateApi candidateApi) {
        this.chatClient = chatClientBuilder.build();
        this.repository = repository;
        this.jobApi = jobApi;
        this.resumeApi = resumeApi;
        this.candidateApi = candidateApi;
    }

    @ApplicationModuleListener
    public void onCvUploaded(CvUploadedEvent event) {
        log.info("Received event for Resume ID: {}", event.resumeId());

        try {
            String analysisResult;

            if (event.jobId() != null) {
                log.info("Targeted analysis requested for Job ID: {}", event.jobId());
                JobResponseDTO job = jobApi.getJobDetails(event.jobId());
                analysisResult = analyzeCv(event.extractedText(), job);
            } else {
                analysisResult = analyzeCv(event.extractedText(), null);
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

    @ApplicationModuleListener
    public void onCandidateUploaded(CandidateUploadedEvent event) {
        log.info("Received AI Analysis event for Candidate ID: {}", event.candidateId());

        try {
            JobResponseDTO job = jobApi.getJobDetails(event.jobId());
            String analysisResult = analyzeTargetedCv(event.extractedText(), job);
            Integer score = null;
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(analysisResult);
                if (root.has("score")) {
                    score = root.get("score").asInt();
                }
            } catch (Exception e) {
                log.warn("Could not parse score from LLM response for Candidate {}", event.candidateId());
            }

            candidateApi.updateCandidateAnalysis(event.candidateId(), score, analysisResult);
            log.info("Candidate {} analysis completed and saved successfully", event.candidateId());

        } catch (Exception e) {
            log.error("Failed to analyze CV for Candidate ID: {}", event.candidateId(), e);
        }
    }

    @Override
    public String analyzeCv(String cvText, JobResponseDTO job) {
        String jobContext = "";
        if (job != null) {
            jobContext = """

                    **Target Job:**
                    - Title: %s
                    - Company: %s
                    - Description: %s
                    """.formatted(job.title(), job.company(), job.description());
        }

        String systemPrompt = """
                You are an expert ATS (Applicant Tracking System) and Career Coach.
                Analyze the provided Resume%s.
                Your audience is the CANDIDATE themselves. Give them actionable advice to improve their CV.
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
           \s""".formatted(jobContext.isEmpty() ? "" : " against the Job Target" + jobContext);
        System.out.println("⏳ Sending request to AI...");
         var cleanResposne = cleanJsonResponse(chatClient.prompt()
                .system(systemPrompt)
                .user(cvText)
                .call()
                .content());
        System.out.println("✅ AI Responded!");
        return cleanResposne;
    }

    @Override
    public String analyzeTargetedCv(String cvText, JobResponseDTO job) {
        String systemPrompt = """
        You are an Elite Tech Recruiter and a state-of-the-art Applicant Tracking System (ATS).
        Analyze the candidate's resume against the specific job posting. Your audience is the HR manager.
        Generate 3-4 highly specific technical or behavioral interview questions to ask this candidate based on their missing skills or weak points. Do NOT give advice to the candidate.

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
          "interviewGuide": [
            { "topic": "Skill or domain", "suggestedQuestion": "Specific question to ask", "reason": "Why ask this" }
          ]
        }
        """.formatted(job.title(), job.company(), job.description());

        return cleanJsonResponse(chatClient.prompt()
                .system(systemPrompt)
                .user(cvText)
                .call()
                .content());
    }

    private String cleanJsonResponse(String response) {
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

    @Override
    public CvAnalysisResult getAnalysisById(UUID analysisId, UUID userId) {
        return repository.findByIdAndUserId(analysisId, userId)
                .orElseThrow(() -> new RuntimeException("Analysis not found or access denied: " + analysisId));
    }

    @Override
    public void deleteAnalysis(UUID analysisId, UUID userId) {
        CvAnalysisResult result = repository.findByIdAndUserId(analysisId, userId)
                .orElseThrow(() -> new RuntimeException("Analysis not found or access denied: " + analysisId));
        repository.delete(result);
        log.info("Analysis deleted successfully: {} by user: {}", analysisId, userId);
    }

    @Override
    public Page<CvAnalysisHistoryDTO> getAnalysisHistory(UUID userId, String keyword, Pageable pageable) {
        Page<CvAnalysisResult> results;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            java.util.List<UUID> resumeIds = new java.util.ArrayList<>(resumeApi.getResumeIdsByUserIdAndKeyword(userId, keyword));
            java.util.List<UUID> jobIds = new java.util.ArrayList<>(jobApi.getJobIdsByUserIdAndKeyword(userId, keyword));
            
            if (resumeIds.isEmpty() && jobIds.isEmpty()) {
                return Page.empty(pageable);
            }
            
            if (resumeIds.isEmpty()) resumeIds.add(UUID.randomUUID());
            if (jobIds.isEmpty()) jobIds.add(UUID.randomUUID());
            
            results = repository.findByUserIdAndResumeOrJobTarget(userId, resumeIds, jobIds, pageable);
        } else {
            results = repository.findAllByUserIdOrderByAnalyzedAtDesc(userId, pageable);
        }

        return results.map(result -> {
            String resumeName = resumeApi.getResumeFileName(result.getResumeId());
            String jobTitle = result.getJobId() != null ? jobApi.getJobTitle(result.getJobId()) : "General Analysis";
            return new CvAnalysisHistoryDTO(
                    result.getId(),
                    result.getAnalyzedAt(),
                    resumeName,
                    jobTitle,
                    result.getFeedback()
            );
        });
    }

    @Override
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
        prompt.append("    \"candidateName\": \"<extract_the_candidate_full_name_from_the_resume_text>\",\n");
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

    @Override
    public CvAnalysisResult startAnalysis(UUID resumeId, UUID jobId, UUID userId) {
        resumeApi.verifyResumeOwnership(resumeId, userId);
        jobApi.verifyJobOwnership(jobId, userId);
        List<ResumeTextDTO> resumes = resumeApi.getResumesText(List.of(resumeId));
        if (resumes.isEmpty()) {
            throw new IllegalArgumentException("No resume found with id: " + resumeId);
        }
        ResumeTextDTO resumeText = resumes.get(0);
        JobResponseDTO job = jobApi.getJobDetails(jobId);

        String feedback = isCurrentUserCompany()
                ? analyzeTargetedCv(resumeText.extractedText(), job)
                : analyzeCv(resumeText.extractedText(), job);

        CvAnalysisResult result = CvAnalysisResult.builder()
                .resumeId(resumeId)
                .jobId(jobId)
                .userId(userId)
                .feedback(feedback)
                .build();
        return repository.save(result);
    }

    private boolean isCurrentUserCompany() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"));
    }


}
