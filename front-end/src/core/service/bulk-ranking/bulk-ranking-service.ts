import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment.development';
import {IBulkRankingResponse, IBulkRankRequest} from '../../model/bulkrank.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BulkRankingService {

  private readonly http = inject(HttpClient)
  private readonly  apiUrl = environment.apiUrl

  startBulkAnalyze(request : IBulkRankRequest) : Observable<IBulkRankingResponse[]> {
    return this.http.post<IBulkRankingResponse[]>(`${this.apiUrl}/v1/analysis/bulk-rank`,request);
  }


}
