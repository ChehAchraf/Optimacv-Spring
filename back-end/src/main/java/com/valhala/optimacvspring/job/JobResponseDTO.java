package com.valhala.optimacvspring.job;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobResponseDTO(
        UUID id,
        String title,
        String company,
        String description,
        LocalDateTime createdAt
) {}
