package com.valhala.optimacvspring.resume.api;

import com.valhala.optimacvspring.resume.dto.ResumeTextDTO;

import java.util.List;
import java.util.UUID;

public interface ResumeApi {

    List<ResumeTextDTO> getResumesText(List<UUID> resumeIds);

}
