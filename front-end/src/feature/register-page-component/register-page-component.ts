import { Component, inject, OnDestroy } from '@angular/core';
import { RouterLink } from "@angular/router";
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { AuthService } from '../../core/service/auth/auth-service';
import { IRegisterRequest } from '../../core/model/user.model';
import { HttpErrorResponse } from '@angular/common/http';
import {ToasterService} from '../../core/service/toast/toaster-service';
import {toast} from 'ngx-sonner';
import {AuthStore} from '../../core/store/auth.store';

@Component({
  selector: 'app-register-page-component',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './register-page-component.html',
  styleUrl: './register-page-component.css',
})
export class RegisterPageComponent  {

  private readonly authStore = inject(AuthStore)
  private readonly fb = inject(FormBuilder)

  registerForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    company: [false]
  });



  onSubmit() {
    if (this.registerForm.valid) {
      this.authStore.register({
        email : this.registerForm.value.email!,
        password : this.registerForm.value.password!,
        role : this.registerForm.value.company ? 'ROLE_COMPANY' : 'ROLE_USER'
      })
    }
  }



}
