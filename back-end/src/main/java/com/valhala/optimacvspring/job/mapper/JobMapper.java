package com.valhala.optimacvspring.job.mapper;

import com.valhala.optimacvspring.job.JobResponseDTO;
import com.valhala.optimacvspring.job.entities.JobTarget;
import com.valhala.optimacvspring.job.dto.JobRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JobMapper {

    JobResponseDTO toResponseDTO(JobTarget jobTarget);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "userId", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "deleted", ignore = true)
    void updateJobTargetFromDto(JobRequestDTO dto, @MappingTarget JobTarget entity);
}
