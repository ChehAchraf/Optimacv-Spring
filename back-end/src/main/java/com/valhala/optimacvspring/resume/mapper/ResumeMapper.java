package com.valhala.optimacvspring.resume.mapper;

import com.valhala.optimacvspring.resume.dto.ResumeResponseDTO;
import com.valhala.optimacvspring.resume.entites.Resume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResumeMapper {

    @Mapping(target = "statusMessage", constant = "your cv has been uploaded successfully, we are analyzing it now")
    ResumeResponseDTO toResponseDTO(Resume resume);

}
