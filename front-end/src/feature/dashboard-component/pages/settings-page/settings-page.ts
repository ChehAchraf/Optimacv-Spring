import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors} from '@angular/forms';
import {LucideAngularModule, Lock,Mail,Shield,User,Key,CheckCircle} from 'lucide-angular';
import {AuthStore} from '../../../../core/store/auth.store';
import {IUpdateProfileRequest, IChangePasswordRequest} from '../../../../core/model/user.model';

@Component({
  selector: 'app-settings-page',
  imports: [
    LucideAngularModule,
    ReactiveFormsModule
  ],
  templateUrl: './settings-page.html',
  styleUrl: './settings-page.css',
})
export class SettingsPage implements OnInit {

  private fb = inject(FormBuilder);
  readonly authStore = inject(AuthStore)


  readonly lock = Lock
  readonly mail = Mail
  readonly shield = Shield
  readonly user = User
  readonly key = Key
  readonly checkCircle = CheckCircle

  accountForm = this.fb.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
  });

  securityForm = this.fb.group({
    currentPassword: ['', Validators.required],
    newPassword: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', Validators.required],
  }, { validators: this.passwordMatchValidator });

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const newPassword = control.get('newPassword')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return newPassword === confirmPassword ? null : { mismatch: true };
  }

  ngOnInit() {
    this.accountForm.patchValue({
      firstName: this.authStore.firstName() || '',
      lastName: this.authStore.lastName() || '',
      email: this.authStore.email() || ''
    });
  }

  onSubmit(){
    if(this.accountForm.valid){
      const data : IUpdateProfileRequest = this.accountForm.value as IUpdateProfileRequest
      this.authStore.update(data)
    } else {
      this.accountForm.markAllAsTouched()
    }
  }

  onChangePassword() {
    if (this.securityForm.valid) {
      const data: IChangePasswordRequest = {
        currentPassword: this.securityForm.value.currentPassword as string,
        newPassword: this.securityForm.value.newPassword as string,
      };
      this.authStore.changePassword(data);
      this.securityForm.reset();
    } else {
      this.securityForm.markAllAsTouched();
    }
  }

}
