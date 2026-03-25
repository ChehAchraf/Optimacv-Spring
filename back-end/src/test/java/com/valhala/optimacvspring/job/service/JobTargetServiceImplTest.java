package com.valhala.optimacvspring.job.service;

import com.valhala.optimacvspring.job.JobResponseDTO;
import com.valhala.optimacvspring.job.dto.JobRequestDTO;
import com.valhala.optimacvspring.job.entities.JobTarget;
import com.valhala.optimacvspring.job.mapper.JobMapper;
import com.valhala.optimacvspring.job.repository.JobTargetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class JobTargetServiceImplTest {
    @Mock
    private JobTargetRepository jobTargetRepository;

    @Mock
    private JobMapper jobMapper;

    @InjectMocks
    private JobTargetServiceImpl jobTargetService;

    @Test
    void shouldntUpdateTheJobTargetIfItsDeleted() {
        UUID userId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();

        JobTarget job = JobTarget.builder().id(jobId).userId(userId).deleted(true).build();

        JobTarget newJob = JobTarget.builder().title("just test").company("test").description("test me ").build();

        when(jobTargetRepository.findById(jobId)).thenReturn(Optional.of(job));

        RuntimeException exception = assertThrows(RuntimeException.class, ()->{
            jobTargetService.updateJobTarget(jobId,newJob);
        });

        verify(jobTargetRepository, never()).save(any(JobTarget.class));
    }


    @Test
    void ShouldCreateJob(){

        UUID userId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();

        var jobToAdd = JobTarget.builder().id(jobId).title("hello").company("hello").description("test").createdAt(LocalDateTime.now()).build();

        var jobDTOToSave = new JobRequestDTO(jobToAdd.getTitle(),jobToAdd.getCompany(),jobToAdd.getDescription());

        var expectedResponse = new JobResponseDTO(jobId,jobToAdd.getTitle(),jobToAdd.getCompany(),jobToAdd.getDescription(),jobToAdd.getCreatedAt());

        when(jobTargetRepository.save(any(JobTarget.class))).thenReturn(jobToAdd);

        when(jobMapper.toResponseDTO(any(JobTarget.class))).thenReturn(expectedResponse);

        var result = jobTargetService.createJob(jobDTOToSave,userId);

        assertNotNull(result,"this is not null");



    }

    @Test
    void shouldDeteleJob(){
        UUID userId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();

        var jobToDelete = JobTarget.builder().id(jobId).userId(userId).build();

        when(jobTargetRepository.findById(jobId)).thenReturn(Optional.of(jobToDelete));

        jobTargetService.deleteJob(jobId,userId);

        verify(jobTargetRepository, times(1)).delete(jobToDelete);
    }

    @Test
    void shouldReturnJobTitle(){
        UUID jobId = UUID.randomUUID();

        var jobToretunTitle = JobTarget.builder().id(jobId).title("title").build();

        when(jobTargetRepository.findById(jobId)).thenReturn(Optional.of(jobToretunTitle));

        var result = jobTargetService.getJobTitle(jobId);

        assertNotNull(result,"its not null");
        assertEquals("title", result);
    }

    @Test
    void shouldReturnFalse(){
        UUID jobId = UUID.randomUUID();

        var jobToFind = JobTarget.builder().id(jobId).build();

        when(jobTargetRepository.existsById(jobId)).thenReturn(false);

        var result = jobTargetService.checkIfJobExists(jobId);

        assertFalse(result);
    }

    @Test
    void shouldReturnJobDetails(){
        UUID jobId = UUID.randomUUID();

        var mockedJob = JobTarget.builder().id(jobId).title("job").description("youcode").description("test").createdAt(LocalDateTime.now()).build();

        var mockedDtoResponse = new JobResponseDTO(mockedJob.getId(),mockedJob.getTitle(),mockedJob.getCompany(),mockedJob.getDescription(),mockedJob.getCreatedAt());

        when(jobTargetRepository.findById(jobId)).thenReturn(Optional.of(mockedJob));
        when(jobMapper.toResponseDTO(any(JobTarget.class))).thenReturn(mockedDtoResponse);

        var result = jobTargetService.getJobDetails(jobId);

        verify(jobTargetRepository,times(1)).findById(jobId);

        assertEquals("job",result.title());

    }

}