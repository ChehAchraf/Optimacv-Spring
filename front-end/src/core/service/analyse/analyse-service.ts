import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment.development';
import {IAnalyseRequest, IAnalysisHistory, IPageResponse} from '../../model/analyse.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AnalyseService {

  private readonly http = inject(HttpClient)
  private readonly apiUrl = environment.apiUrl


  startAnalysis (request : IAnalyseRequest) : Observable<IAnalysisHistory>{
    return this.http.post<IAnalysisHistory>(`${this.apiUrl}/v1/analysis/start`,request)
  }

  getanalysis(analysisId : string) : Observable<IAnalysisHistory>{
    return this.http.get<IAnalysisHistory>(`${this.apiUrl}/v1/analysis/${analysisId}`)
  }

  getHistory(page: number = 0, size: number = 10, keyword?: string) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (keyword && keyword.trim().length > 0) {
      params = params.set('keyword', keyword.trim());
    }

    return this.http.get<IPageResponse<IAnalysisHistory>>(
      `${this.apiUrl}/v1/analysis/history`,
      { params }
    );
  }

  deleteAnalysis(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/v1/analysis/${id}`);
  }

}
