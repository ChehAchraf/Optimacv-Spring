import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment.development';
import {IResumeRequest, IResumeResponse} from '../../model/resume.model';
import {Observable} from 'rxjs';
import {Page} from '../../model/page.model';
import {HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ResumeService {

  private readonly http = inject(HttpClient)
  private readonly apiUrl = environment.apiUrl

  uploadResume(request : IResumeRequest) : Observable<IResumeResponse>{
    const formData = new FormData()

    formData.append('file', request.file);

    if(request.jobId){
      formData.append('jobId',request.jobId);
    }

    return this.http.post<IResumeResponse>(`${this.apiUrl}/v1/resumes/upload`,formData);
  }

  getMyResumes(page: number = 0, size: number = 10): Observable<Page<IResumeResponse>>{
    let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());
    return this.http.get<Page<IResumeResponse>>(`${this.apiUrl}/v1/resumes/my-resumes`, { params });
  }

}
