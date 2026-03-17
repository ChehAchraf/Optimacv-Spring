package com.valhala.optimacvspring.analysis.repository;

import com.valhala.optimacvspring.analysis.entities.CvAnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CvAnalysisResultRepository extends JpaRepository<CvAnalysisResult, UUID> {

    Optional<CvAnalysisResult> findByResumeId(UUID resumeId);

    @Query("""
            SELECT COUNT(car)
            FROM CvAnalysisResult car
            WHERE car.resumeId IN (
                SELECT r.id FROM Resume r WHERE r.userId = :userId
            )
            """)
    long countAnalysesByUserId(@Param("userId") UUID userId);

    @Query("""
            SELECT COALESCE(AVG(0.0), 0.0)
            FROM CvAnalysisResult car
            WHERE car.resumeId IN (
                SELECT r.id FROM Resume r WHERE r.userId = :userId
            )
            """)
    double findAverageScoreByUserId(@Param("userId") UUID userId);
}
