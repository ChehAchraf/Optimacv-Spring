export interface IBulkRankRequest {
  jobId : string;
  resumeIds : string[]
}

export interface IBulkRankingResponse {
  resumeId : string,
  candidateName : string,
  rank : number,
  matchScore : number ,
  reason : string,
  missingSkills : string[]
}

