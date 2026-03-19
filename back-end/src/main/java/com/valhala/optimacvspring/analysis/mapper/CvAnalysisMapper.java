package com.valhala.optimacvspring.analysis.mapper;

import com.valhala.optimacvspring.analysis.dto.CvAnalysisHistoryDTO;
import com.valhala.optimacvspring.analysis.dto.CvAnalysisResponseDTO;
import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CvAnalysisMapper {


    CvAnalysisResponseDTO toResponseDTO(CvAnalysisResult result);

    default CvAnalysisHistoryDTO toHistoryDTO(CvAnalysisResult result, String resumeFileName, String jobTitle) {
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
