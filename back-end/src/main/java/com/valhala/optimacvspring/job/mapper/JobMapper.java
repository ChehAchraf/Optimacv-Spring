package com.valhala.optimacvspring.job.mapper;

import com.valhala.optimacvspring.job.JobResponseDTO;
import com.valhala.optimacvspring.job.entities.JobTarget;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {

    JobResponseDTO toResponseDTO(JobTarget jobTarget);
}
