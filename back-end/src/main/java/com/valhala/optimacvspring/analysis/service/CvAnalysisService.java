package com.valhala.optimacvspring.analysis.service;

import com.valhala.optimacvspring.analysis.dto.BulkRankingRequestDTO;
import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import com.valhala.optimacvspring.analysis.repository.CvAnalysisResultRepository;
import com.valhala.optimacvspring.job.api.JobApi;
import com.valhala.optimacvspring.job.dto.JobResponseDTO;
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

    public String analyzeTargetedCv(String cvText, JobResponseDTO job) {
        String systemPrompt = """
            You are an Elite Tech Recruiter and a state-of-the-art Applicant Tracking System (ATS).
            You have been given a candidate's resume and a specific job posting they want to apply for.
            
            **Target Job Information:**
            - **Job Title:** %s
            - **Company:** %s
            - **Job Description:** %s
            
            Your task is to perform a rigorous, targeted analysis of the resume against this specific job.
            
            Please provide your analysis strictly in the following structured format, using Markdown:
            
            ### 1. Match Score & Verdict
            Provide a percentage score (e.g., 85%%) of how well this CV fits the target job. Add a 2-sentence verdict explaining if the candidate is a Strong Match, Partial Match, or Weak Match.
            
            ### 2. Matching Skills
            List the specific skills, technologies, and experiences from the CV that directly match the job requirements. Be precise and reference both the CV content and the job description.
            
            ### 3. Missing Keywords & Gaps
            Identify crucial skills, qualifications, and keywords required by the job description that are completely missing or insufficiently represented in the CV. This is critical for ATS optimization.
            
            ### 4. Tailoring Advice
            Provide 3 to 5 specific, actionable steps the candidate must take to tailor this exact CV for this exact role. Include concrete suggestions for rewording bullet points, adding missing keywords, and restructuring sections.
            
            Maintain a professional, objective, and highly analytical tone. Every piece of advice must be specific to this job-CV combination.
            """.formatted(job.title(), job.company(), job.description());

        return chatClient.prompt()
                .system(systemPrompt)
                .user(cvText)
                .call()
                .content();
    }

    public CvAnalysisResult getAnalysisByResumeId(UUID resumeId) {
        return repository.findByResumeId(resumeId)
                .orElseThrow(() -> new RuntimeException("your cv is not analyzed yet " + resumeId));
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


}