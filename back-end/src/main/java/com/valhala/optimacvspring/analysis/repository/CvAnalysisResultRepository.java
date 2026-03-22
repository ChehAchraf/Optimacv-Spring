package com.valhala.optimacvspring.analysis.repository;

import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CvAnalysisResultRepository extends JpaRepository<CvAnalysisResult, UUID> {

    Optional<CvAnalysisResult> findByResumeId(UUID resumeId);

    Optional<CvAnalysisResult> findByIdAndUserId(UUID id, UUID userId);

    Page<CvAnalysisResult> findAllByUserIdOrderByAnalyzedAtDesc(UUID userId, Pageable pageable);

    @Query("SELECT c FROM CvAnalysisResult c WHERE c.userId = :userId AND (c.resumeId IN :resumeIds OR c.jobId IN :jobIds) ORDER BY c.analyzedAt DESC")
    Page<CvAnalysisResult> findByUserIdAndResumeOrJobTarget(@org.springframework.data.repository.query.Param("userId") UUID userId, @org.springframework.data.repository.query.Param("resumeIds") java.util.List<UUID> resumeIds, @org.springframework.data.repository.query.Param("jobIds") java.util.List<UUID> jobIds, Pageable pageable);

    long countByUserId(UUID userId);

    @Query("SELECT COALESCE(AVG(0.0), 0.0) FROM CvAnalysisResult car WHERE car.userId = :userId")
    double findAverageScoreByUserId(@org.springframework.data.repository.query.Param("userId") UUID userId);
}
