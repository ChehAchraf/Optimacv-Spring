package com.valhala.optimacvspring.resume.events;

import java.util.UUID;

public record CvUploadedEvent(UUID resumeId, String extractedText, UUID jobId) {
}
