package com.valhala.optimacvspring.analysis.mapper;

import com.valhala.optimacvspring.analysis.dto.CvAnalysisHistoryDTO;
import com.valhala.optimacvspring.analysis.dto.CvAnalysisResponseDTO;
import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import com.valhala.optimacvspring.job.api.JobApi;
import com.valhala.optimacvspring.resume.api.ResumeApi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CvAnalysisMapper {

    @Autowired
    protected ResumeApi resumeApi;

    @Autowired
    protected JobApi jobApi;

    @Mapping(target = "resumeName", expression = "java(result.getResumeId() != null ? resumeApi.getResumeFileName(result.getResumeId()) : \"Unknown\")")
    @Mapping(target = "jobTitle", expression = "java(result.getJobId() != null ? jobApi.getJobTitle(result.getJobId()) : \"General Analysis\")")
    public abstract CvAnalysisResponseDTO toResponseDTO(CvAnalysisResult result);

    public CvAnalysisHistoryDTO toHistoryDTO(CvAnalysisResult result, String resumeFileName, String jobTitle) {
        if (result == null) {
            return null;
        }
        return new CvAnalysisHistoryDTO(
                result.getId(),
                result.getAnalyzedAt(),
                resumeFileName,
                jobTitle,
                result.getFeedback()
        );
    }
}
