import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private readonly http = inject(HttpClient);
  private readonly api = "http://localhost:8080/api/v1/companies/jobs";

  bulkUploadResumes(jobId: string, files: File[]): Observable<void> {
    const formData = new FormData();
    files.forEach(file => {
      formData.append('files', file);
    });
    
    return this.http.post<void>(`${this.api}/${jobId}/bulk-upload`, formData);
  }

  getCandidates(jobId: string, page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.api}/${jobId}/candidates?page=${page}&size=${size}`);
  }

  getCompanyJobs(): Observable<{ content: { id: string, title: string }[] }> {
    return this.http.get<{ content: { id: string, title: string }[] }>(`http://localhost:8080/api/v1/jobs?size=100`);
  }

  getCandidateDetails(candidateId: string): Observable<any> {
    return this.http.get<any>(`http://localhost:8080/api/v1/companies/candidates/${candidateId}`);
  }

  deleteCandidate(candidateId: string): Observable<void> {
    return this.http.delete<void>(`http://localhost:8080/api/v1/companies/candidates/${candidateId}`);
  }
}
