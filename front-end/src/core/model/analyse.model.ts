export interface IAnalyseRequest {
  resumeId: string
  jobId: string
}

export interface IAnalysisHistory {
  id: string;
  analysisId: string;
  analyzedAt: string;
  resumeFileName: string;
  jobTitle: string;
  feedback: string;
}

export interface IPageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
