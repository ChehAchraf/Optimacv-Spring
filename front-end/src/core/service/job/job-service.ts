import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment.development';
import {IJobRequest, IJobResponse} from '../../model/job.model';
import {Observable} from 'rxjs';

import {HttpParams} from '@angular/common/http';
import {Page} from '../../model/page.model';

@Injectable({
  providedIn: 'root',
})
export class JobService {

  private http = inject(HttpClient)
  private apiUrl = environment.apiUrl


  createJob(request : IJobRequest) : Observable<IJobResponse>{
    return this.http.post<IJobResponse>(`${this.apiUrl}/v1/jobs`,request)
  }

  loadMyJobs(page: number = 0, size: number = 5, keyword? : string) : Observable<Page<IJobResponse>>{
    let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());
    if (keyword) {
      params = params.set('keyword', keyword);
    }
    return this.http.get<Page<IJobResponse>>(`${this.apiUrl}/v1/jobs/my-jobs`, { params });
  }

  deleteMyJob(jobId : string) : Observable<void>{
    return this.http.delete<void>(`${this.apiUrl}/v1/jobs/${jobId}`)
  }

}
