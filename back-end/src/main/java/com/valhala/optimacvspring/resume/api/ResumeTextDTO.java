package com.valhala.optimacvspring.resume.dto;

import java.util.UUID;

public record ResumeTextDTO(
        UUID resumeId, String extractedText
) {
}
