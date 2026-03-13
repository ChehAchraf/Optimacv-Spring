import {Component, inject} from '@angular/core';
import { RouterLink } from "@angular/router";
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-register-page-component',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './register-page-component.html',
  styleUrl: './register-page-component.css',
})
export class RegisterPageComponent {

  private fb = inject(FormBuilder)

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    company : [false]
  });



  onSubmit() {
    if (this.loginForm.valid) {
      console.log('the data :', this.loginForm.value);
    }
  }

}
