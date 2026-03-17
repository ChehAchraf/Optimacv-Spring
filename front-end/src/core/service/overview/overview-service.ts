import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment.development';
import {Observable} from 'rxjs';
import {IOverview} from '../../model/overview.model';

@Injectable({
  providedIn: 'root',
})
export class OverviewService {

  private http = inject(HttpClient)
  private apiUrl = environment.apiUrl


  getOverView() : Observable<IOverview>{
    return this.http.get<IOverview>(`${this.apiUrl}/v1/jobs/overview`);
  }

}
