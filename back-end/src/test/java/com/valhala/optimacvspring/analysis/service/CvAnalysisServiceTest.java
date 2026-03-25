package com.valhala.optimacvspring.analysis.service;

import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import com.valhala.optimacvspring.analysis.repository.CvAnalysisResultRepository;
import com.valhala.optimacvspring.candidate.api.CandidateApi;
import com.valhala.optimacvspring.job.JobResponseDTO;
import com.valhala.optimacvspring.job.api.JobApi;
import com.valhala.optimacvspring.job.entities.JobTarget;
import com.valhala.optimacvspring.job.repository.JobTargetRepository;
import com.valhala.optimacvspring.job.service.JobTargetService;
import com.valhala.optimacvspring.resume.api.ResumeApi;
import com.valhala.optimacvspring.resume.events.CvUploadedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CvAnalysisServiceTest {

    @Mock
    private JobTargetRepository jobTargetRepository;
    @Mock
    private JobApi jobApi;
    @Mock
    private ChatClient.Builder chatClientBuilder;
    @Mock
    private ChatClient chatClient;
    @Mock
    private CvAnalysisResultRepository repository;
    @Mock
    private ResumeApi resumeApi;
    @Mock
    private CandidateApi candidateApi;

    @InjectMocks
    private CvAnalysisService cvAnalysisService;



    @BeforeEach
    void setUp(){
        chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);

        when(chatClient.prompt()
                .system(anyString())
                .user(anyString())
                .call()
                .content())
                .thenReturn("هادا هو الفيدباك المزور لي بغينا نرجعو");

        when(chatClientBuilder.build()).thenReturn(chatClient);
        cvAnalysisService = new CvAnalysisService(
                chatClientBuilder,
                repository,
                jobApi,
                resumeApi,
                candidateApi
        );
    }

    @Test
    void onCvUploaded() {
        UUID resumeId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();
        var mockedJob = JobTarget.builder().id(jobId).title("js").company("youcode").description("test").createdAt(LocalDateTime.now()).build();
        var mockedUploadedCv = new CvUploadedEvent(resumeId,"hello",jobId);
        var mockedJobResponseDto = new JobResponseDTO(jobId,mockedJob.getTitle(),mockedJob.getCompany(),mockedJob.getDescription(),mockedJob.getCreatedAt());
        var mockedResult = CvAnalysisResult.builder().jobId(jobId).build();

        when(jobApi.getJobDetails(jobId)).thenReturn(mockedJobResponseDto);

        cvAnalysisService.onCvUploaded(mockedUploadedCv);

        verify(repository).save(argThat(result -> result.getJobId().equals(jobId)));

    }

    @Test
    void onCandidateUploaded() {
    }

    @Test
    void analyzeCv() {
    }

    @Test
    void analyzeTargetedCv() {
    }

    @Test
    void getAnalysisById() {
    }

    @Test
    void deleteAnalysis() {
    }

    @Test
    void getAnalysisHistory() {
    }

    @Test
    void rankMultipleResumes() {
    }

    @Test
    void startAnalysis() {
    }
}