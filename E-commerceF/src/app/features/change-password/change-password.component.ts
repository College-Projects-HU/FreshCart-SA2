import { Component, computed, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from '../../core/auth/services/auth.service';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-change-password',
  imports: [RouterLink,ReactiveFormsModule],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css',
})
export class ChangePasswordComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  loading:boolean = false;
  logged = computed(()=>this.authService.isLogged());
  changeSubscribe: Subscription = new Subscription();
  changePasswordForm: FormGroup = this.fb.nonNullable.group(
    {
      currentPassword: ['', Validators.required],
      password: [
        '',
        [
          Validators.required,
          Validators.pattern(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/),
        ],
      ],
      rePassword: ['', Validators.required],
    },
    { validators: [this.checkPassword] },
  );
  ngOnInit(): void {}

  checkPassword(group: AbstractControl): any {
    const newpassword = group.get('password')?.value;
    const confirmPassword = group.get('rePassword')?.value;
    if (newpassword !== confirmPassword && confirmPassword !== '') {
      group.get('rePassword')?.setErrors({ mismatch: true });
      return { mismatch: true };
    }
    return null;
  }
  changeUserPassword(): void {
    if (this.changePasswordForm.valid) {
      const { currentPassword, password , rePassword } = this.changePasswordForm.value;
      this.loading = true;
      this.changeSubscribe = this.authService.changePassword({ currentPassword, password ,  rePassword }).subscribe({
        next: (res) => {
          console.log(res);
          if (res.message === 'success') {
            this.router.navigate(['/login']);
            this.authService.isLogged.set(false);
            localStorage.removeItem('AccessToken');
            localStorage.removeItem('RefreshToken');
            localStorage.removeItem('userToken');
          }
        },
        error: (err) => {
          console.log(err);
          this.changePasswordForm.reset();
        },
        complete: () => {
          this.changePasswordForm.reset();
        },
      });
    }
  }
}
