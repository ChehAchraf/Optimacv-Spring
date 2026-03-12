package com.valhala.optimacvspring.resume.api;

import java.util.List;
import java.util.UUID;

public interface ResumeApi {

    List<ResumeTextDTO> getResumesText(List<UUID> resumeIds);

}
