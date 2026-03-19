package com.valhala.optimacvspring.resume.repository;

import com.valhala.optimacvspring.resume.entites.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID> {

    Page<Resume> findAllByUserId(UUID userId, Pageable pageable);

    List<Resume> findAllByIdIn(List<UUID> ids);
}
