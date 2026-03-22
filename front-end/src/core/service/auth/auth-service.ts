import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {
  ILoginRequest,
  ILoginResponse,
  IRegisterRequest,
  IUpdateProfileRequest,
  IUserProfileResponse,
  IChangePasswordRequest
} from '../../model/user.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private readonly http = inject(HttpClient)
  private readonly api = "http://localhost:8080/api/v1"

  login(request: ILoginRequest): Observable<ILoginResponse> {
    return this.http.post<ILoginResponse>(`${this.api}/auth/login`, request)
  }

  register(request: IRegisterRequest): Observable<ILoginResponse> {
    return this.http.post<ILoginResponse>(`${this.api}/auth/register`, request)
  }

  updateProfile(request: IUpdateProfileRequest): Observable<IUserProfileResponse> {
    return this.http.put<IUserProfileResponse>(`${this.api}/profile`, request)
  }

  changePassword(request: IChangePasswordRequest): Observable<string> {
    return this.http.put(`${this.api}/profile/password`, request, { responseType: 'text' });
  }

  getProfile() {
    return this.http.get<IUserProfileResponse>('/api/v1/profile');
  }

}
