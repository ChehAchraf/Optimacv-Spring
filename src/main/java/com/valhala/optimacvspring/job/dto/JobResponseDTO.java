package com.valhala.optimacvspring.job.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobResponseDTO(
        UUID id,
        String title,
        String company,
        String description,
        LocalDateTime createdAt
) {}
