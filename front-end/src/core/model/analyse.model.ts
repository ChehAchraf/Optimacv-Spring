export interface IAnalyseRequest {
  resumeId: string
  jobId: string
}

export interface IAnalysisDetails {
  score: number;
  verdict: string;
  matchingSkills: string[];
  missingKeywords: string[];
  resumeFileName?: string;
  actionPlan: { title: string; description: string }[];
}

export interface IAnalysisHistory {
  id: string;
  analysisId: string;
  analyzedAt?: string;
  resumeName?: string;
  jobTitle?: string;
  feedback: string;
  fullAnalysis?: IAnalysisDetails;
  parsedScore?: number;
}

export interface IPageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
