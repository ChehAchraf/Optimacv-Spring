package com.valhala.optimacvspring.candidate.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CandidateResponseDTO(
        UUID id,
        String originalFileName,
        Integer matchScore,
        String aiFeedback,
        LocalDateTime uploadedAt
) {}
