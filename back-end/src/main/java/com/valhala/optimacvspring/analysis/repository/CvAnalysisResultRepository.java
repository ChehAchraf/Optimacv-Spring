package com.valhala.optimacvspring.analysis.repository;

import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CvAnalysisResultRepository extends JpaRepository<CvAnalysisResult, UUID> {

    Optional<CvAnalysisResult> findByResumeId(UUID resumeId);

}
