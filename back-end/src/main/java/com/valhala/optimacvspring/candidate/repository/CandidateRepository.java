package com.valhala.optimacvspring.candidate.repository;

import com.valhala.optimacvspring.candidate.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
    List<Candidate> findAllByJobId(UUID jobId);
}
