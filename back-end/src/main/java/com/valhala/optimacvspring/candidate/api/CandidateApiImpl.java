package com.valhala.optimacvspring.candidate.api;

import com.valhala.optimacvspring.candidate.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidateApiImpl implements CandidateApi {
    
    private final CandidateRepository candidateRepository;

    @Override
    @Transactional
    public void updateCandidateAnalysis(UUID candidateId, Integer score, String feedback) {
        candidateRepository.findById(candidateId).ifPresent(candidate -> {
            candidate.setMatchScore(score);
            candidate.setAiFeedback(feedback);
            candidateRepository.save(candidate);
        });
    }
}
