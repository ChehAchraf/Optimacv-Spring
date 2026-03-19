package com.valhala.optimacvspring.job.repository;

import com.valhala.optimacvspring.job.entities.JobTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobTargetRepository extends JpaRepository<JobTarget, UUID> {

    Page<JobTarget> findAllByUserId(UUID userId, Pageable pageable);

    List<JobTarget> findTop3ByUserIdOrderByCreatedAtDesc(UUID userId);

    long countByUserId(UUID userId);

    Page<JobTarget> findAllByUserIdAndTitleContainingIgnoreCase(UUID userId, String title, Pageable pageable);
}
