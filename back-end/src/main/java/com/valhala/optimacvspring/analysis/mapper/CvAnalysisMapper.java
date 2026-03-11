package com.valhala.optimacvspring.analysis.mapper;

import com.valhala.optimacvspring.analysis.dto.CvAnalysisResponseDTO;
import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CvAnalysisMapper {

    CvAnalysisResponseDTO toResponseDTO(CvAnalysisResult result);

}
