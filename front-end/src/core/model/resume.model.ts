
export interface IResumeRequest {
  file: File;
  jobId?: string;
}

export interface IResumeResponse {
  id: string
  fileName: string
  uploadedAt: string
  statusMessage: string
}

