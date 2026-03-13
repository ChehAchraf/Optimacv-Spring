import {Component, inject} from '@angular/core';
import { RouterLink } from "@angular/router";
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-login-page-component',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './login-page-component.html',
  styleUrl: './login-page-component.css',
})
export class LoginPageComponent {
  private fb = inject(FormBuilder)


  loginForm = this.fb.group({
    email : ['',[Validators.required, Validators.email]],
    password : ['', [Validators.required, Validators.minLength(8)]]
  })

  onSubmit() : void{
    if(this.loginForm.valid){
      console.log("data : ", this.loginForm.value)
    }
  }
}
