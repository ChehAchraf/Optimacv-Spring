package com.valhala.optimacvspring.candidate.events;

import java.util.UUID;

public record CandidateUploadedEvent(UUID candidateId, String extractedText, UUID jobId) {
}
