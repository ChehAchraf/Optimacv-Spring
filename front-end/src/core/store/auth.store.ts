import {patchState, signalStore, withComputed, withMethods, withState} from '@ngrx/signals';
import {computed, inject} from '@angular/core';
import {AuthService} from '../service/auth/auth-service';
import {rxMethod} from '@ngrx/signals/rxjs-interop';
import {switchMap} from 'rxjs';
import {tapResponse} from '@ngrx/operators';
import {toast} from 'ngx-sonner';
import {HttpErrorResponse} from '@angular/common/http';

type AuthState = {
  token : string | null;
  role : string | null;
  email : string | null;
}

const initalState : AuthState = {
  token: localStorage.getItem('token'),
  role : localStorage.getItem('role'),
  email : localStorage.getItem('email')

}

export const AuthStore = signalStore(

  {providedIn : 'root'},

  withState(
    initalState
  ),

  withComputed((store) => ({
    isAuthenticated: computed(() => !!store.token()),
    isCompany: computed(() => store.role() === 'ROLE_COMPANY'),
    isUser: computed(() => store.role() === 'ROLE_USER'),
  })),

  withMethods((store) =>{
    const authService = inject(AuthService)
  return {
    login: rxMethod<{ email: string; password: string }>((credentials$) => {
      return credentials$.pipe(
        switchMap((credentials) => {


          return authService.login(credentials).pipe(
            tapResponse({
              next: (response: any) => {
                localStorage.setItem('token', response.token);
                localStorage.setItem('role', response.role);
                localStorage.setItem('email', response.email);

                patchState(store, {
                  token: response.token,
                  role: response.role,
                  email: response.email
                });

                toast.success("seccessfully loged in")
              },
              error: (err:HttpErrorResponse) => {
                console.error('Login failed', err);
                toast.error(`${err.error.message}`)
              },
            })
          );
        })
      );
    }),

    register : rxMethod<{email : string, password : string , role: 'ROLE_USER' | 'ROLE_COMPANY'}>((credentials$)=>{
      return credentials$.pipe(
        switchMap((credentials$)=>{
          return authService.register(credentials$).pipe(
            tapResponse({
              next : (response)=>{
                toast.success("you have successfully signed up, you can login")
              },
              error : (error : HttpErrorResponse)=>{
                console.log("register page error :", error)
                toast.error("error while sign up, please try again later")
              }
            })
          )
        })
      )
    })
  }
  })

)
