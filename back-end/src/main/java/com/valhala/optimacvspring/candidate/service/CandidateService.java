package com.valhala.optimacvspring.candidate.service;

import com.valhala.optimacvspring.candidate.dto.CandidateResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CandidateService {

    void bulkUploadCandidates(UUID companyId, UUID jobId, List<MultipartFile> files);

    Page<CandidateResponseDTO> getCandidatesForJob(UUID companyId, UUID jobId, Pageable pageable);

    CandidateResponseDTO getCandidateByIdForCompany(UUID companyId, UUID candidateId);

    void deleteCandidateForCompany(UUID companyId, UUID candidateId);
}
