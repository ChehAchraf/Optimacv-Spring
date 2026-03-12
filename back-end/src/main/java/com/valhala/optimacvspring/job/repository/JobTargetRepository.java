package com.valhala.optimacvspring.job.repository;

import com.valhala.optimacvspring.job.entities.JobTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobTargetRepository extends JpaRepository<JobTarget, UUID> {

    List<JobTarget> findAllByUserId(UUID userId);


}
