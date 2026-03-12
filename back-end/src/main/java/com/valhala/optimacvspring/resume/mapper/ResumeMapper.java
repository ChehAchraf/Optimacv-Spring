package com.valhala.optimacvspring.resume.mapper;

import com.valhala.optimacvspring.resume.dto.ResumeResponseDTO;
import com.valhala.optimacvspring.resume.entites.Resume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResumeMapper {

    @Mapping(target = "statusMessage", constant = "your cv has been uploaded successfully, you can analyze it after selecting a job.")
    ResumeResponseDTO toResponseDTO(Resume resume);

}
