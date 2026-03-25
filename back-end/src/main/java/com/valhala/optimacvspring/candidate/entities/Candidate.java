package com.valhala.optimacvspring.candidate.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "candidates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID companyId;

    @Column(nullable = false)
    private UUID jobId;

    @Column(nullable = false)
    private String originalFileName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String parsedCvText;

    private Integer matchScore;

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime uploadedAt;
}
