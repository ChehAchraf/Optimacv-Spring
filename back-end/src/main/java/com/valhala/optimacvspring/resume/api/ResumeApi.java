package com.valhala.optimacvspring.resume.api;

import java.util.List;
import java.util.UUID;

public interface ResumeApi {

    List<ResumeTextDTO> getResumesText(List<UUID> resumeIds);

    void verifyResumeOwnership(UUID resumeId, UUID userId);

    String getResumeFileName(UUID resumeId);

    List<UUID> getResumeIdsByUserIdAndKeyword(UUID userId, String keyword);

}
