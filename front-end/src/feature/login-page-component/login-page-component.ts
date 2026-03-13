import {Component, inject, signal} from '@angular/core';
import { RouterLink } from "@angular/router";
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../core/service/auth-service';
import { ILoginRequest } from '../../core/model/user.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login-page-component',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './login-page-component.html',
  styleUrl: './login-page-component.css',
})
export class LoginPageComponent {
  private readonly authService = inject(AuthService)
  private readonly fb = inject(FormBuilder)

  loginError = signal<string>('');


  loginForm = this.fb.group({
    email : ['',[Validators.required, Validators.email]],
    password : ['', [Validators.required, Validators.minLength(8)]]
  })

  onSubmit() : void{
    if (this.loginForm.valid) {
      const formValue = this.loginForm.value;
      
      const request: ILoginRequest = {
        email: formValue.email ?? '',
        password: formValue.password ?? ''
      };

      this.authService.login(request).subscribe({
        next : (data) =>{
          console.log(data)
        },
        error : (error : HttpErrorResponse) =>{
          this.loginError.set('password or email incorrect')
        }
      })
    }
  }



}
