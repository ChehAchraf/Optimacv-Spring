package com.valhala.optimacvspring.resume.api;

import java.util.UUID;

public record ResumeTextDTO(
        UUID resumeId, String extractedText
) {
}
