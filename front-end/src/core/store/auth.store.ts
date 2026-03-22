import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { computed, inject } from '@angular/core';
import { AuthService } from '../service/auth/auth-service';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, switchMap, tap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { toast } from 'ngx-sonner';
import { HttpErrorResponse } from '@angular/common/http';
import { IChangePasswordRequest, IUpdateProfileRequest } from '../model/user.model';
import { Router } from '@angular/router';

type AuthState = {
  token: string | null;
  role: string | null;
  firstName: string | null,
  lastName: string | null,
  email: string | null;
}

const initalState: AuthState = {
  token: localStorage.getItem('token'),
  role: localStorage.getItem('role'),
  firstName: localStorage.getItem('firstName'),
  lastName: localStorage.getItem('lastName'),
  email: localStorage.getItem('email')
}

export const AuthStore = signalStore(

  { providedIn: 'root' },

  withState(
    initalState
  ),

  withComputed((store) => ({
    isAuthenticated: computed(() => !!store.token()),
    isCompany: computed(() => store.role() === 'ROLE_COMPANY'),
    isUser: computed(() => store.role() === 'ROLE_USER'),
  })),

  withMethods((store) => {
    const authService = inject(AuthService)
    const router = inject(Router)
    return {
      login: rxMethod<{ email: string; password: string }>((credentials$) => {
        return credentials$.pipe(
          switchMap((credentials) => {


            return authService.login(credentials).pipe(
              tapResponse({
                next: (response: any) => {
                  console.log(response)
                  localStorage.setItem('token', response.token);
                  localStorage.setItem('role', response.role);
                  localStorage.setItem('email', response.email);
                  localStorage.setItem('firstName',response.firstName);
                  localStorage.setItem('lastName',response.lastName);
                  patchState(store, {
                    token: response.token,
                    role: response.role,
                    email: response.email
                  });

                  toast.success("seccessfully loged in")
                },
                error: (err: HttpErrorResponse) => {
                  console.error('Login failed', err);
                  toast.error(`${err.error.message}`)
                },
              })
            );
          })
        );
      }),

      register: rxMethod<{ email: string, password: string, role: 'ROLE_USER' | 'ROLE_COMPANY' }>((credentials$) => {
        return credentials$.pipe(
          switchMap((credentials$) => {
            return authService.register(credentials$).pipe(
              tapResponse({
                next: (response) => {
                  toast.success("you have successfully signed up, you can login")
                },
                error: (error: HttpErrorResponse) => {
                  console.log("register page error :", error)
                  toast.error("error while sign up, please try again later")
                }
              })
            )
          })
        )
      }),

      update: rxMethod<IUpdateProfileRequest>((response$) => {
        return response$.pipe(
          exhaustMap((request) => {
            const toastId = toast.loading("the request is being processed")
            return authService.updateProfile(request).pipe(
              tapResponse({
                next: (response) => {
                  toast.success("the profile has been updated successfully", { id: toastId })
                  localStorage.setItem('firstName', response.firstName);
                  localStorage.setItem('lastName', response.lastName);
                  localStorage.setItem('email', response.email);
                  patchState(store, {
                    firstName: response.firstName,
                    lastName: response.lastName,
                    email: response.email
                  })
                },
                error: (error: HttpErrorResponse) => {
                  toast.error("there must be an error updating your profile, please try again", { id: toastId })
                }
              })
            )
          })
        )
      }),

      changePassword: rxMethod<IChangePasswordRequest>((request$) => {
        return request$.pipe(
          exhaustMap((request) => {
            const toastId = toast.loading("Processing password change request");
            return authService.changePassword(request).pipe(
              tapResponse({
                next: () => {
                  toast.success("Password changed successfully", { id: toastId });
                },
                error: (error: HttpErrorResponse) => {
                  toast.error(error.error?.message || "Error changing password", { id: toastId });
                }
              })
            );
          })
        );
      }),

      loadProfile : rxMethod<void>((response$)=>{
        return response$.pipe(
          switchMap((response)=>{
            return authService.getProfile().pipe(
              tapResponse({
                next : (response)=>{
                  console.log(response)
                  localStorage.setItem('firstName', response.firstName);
                  localStorage.setItem('lastName', response.lastName);
                  localStorage.setItem('email', response.email);
                  patchState(store,{
                    firstName:response.firstName,
                    lastName : response.lastName,
                    email : response.email
                  })
                },
                error : (error : HttpErrorResponse)=>{

                }
              })
            )
          })
        )
      }),

      logout(){
        localStorage.removeItem('token')
        localStorage.removeItem('email')
        localStorage.removeItem('role')
        localStorage.removeItem('firstName')
        localStorage.removeItem('lastName')

        patchState(store,{
          email : null,
          role : null,
          token : null,
          firstName : null,
          lastName : null
        })

        router.navigate(['/login'])
      }
    }
  })

)
