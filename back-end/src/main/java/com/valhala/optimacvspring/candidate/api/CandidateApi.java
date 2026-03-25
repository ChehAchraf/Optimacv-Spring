package com.valhala.optimacvspring.candidate.api;

import java.util.UUID;

public interface CandidateApi {
    void updateCandidateAnalysis(UUID candidateId, Integer score, String feedback);
}
