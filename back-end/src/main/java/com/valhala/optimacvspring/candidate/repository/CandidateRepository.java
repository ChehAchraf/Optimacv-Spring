package com.valhala.optimacvspring.candidate.repository;

import com.valhala.optimacvspring.candidate.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
    List<Candidate> findAllByJobId(UUID jobId);
    
    @Query("SELECT c FROM Candidate c WHERE c.companyId = :companyId AND c.jobId = :jobId ORDER BY c.matchScore DESC NULLS LAST")
    Page<Candidate> findCandidatesByCompanyIdAndJobId(
            @Param("companyId") UUID companyId, 
            @Param("jobId") UUID jobId, 
            Pageable pageable
    );
}
