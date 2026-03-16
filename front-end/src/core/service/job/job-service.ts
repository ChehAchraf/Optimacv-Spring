import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment.development';
import {IJobRequest, IJobResponse} from '../../model/job.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class JobService {

  private http = inject(HttpClient)
  private apiUrl = environment.apiUrl


  createJob(request : IJobRequest) : Observable<IJobResponse>{
    return this.http.post<IJobResponse>(`${this.apiUrl}/v1/jobs`,request)
  }

  loadMyJobs() : Observable<IJobResponse[]>{
    return this.http.get<IJobResponse[]>(`${this.apiUrl}/v1/jobs/my-jobs`)
  }

}
