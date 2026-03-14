import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ILoginRequest, ILoginResponse, IRegisterRequest} from '../model/user.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private readonly http = inject(HttpClient)
  private readonly api = "http://localhost:8080/api/v1"

  login(request : ILoginRequest) : Observable<ILoginResponse>{
    return this.http.post<ILoginResponse>(`${this.api}/auth/login`,request)
  }

  register(request : IRegisterRequest) : Observable<ILoginResponse>{
    return this.http.post<ILoginResponse>(`${this.api}/auth/register`, request)
  }

}
