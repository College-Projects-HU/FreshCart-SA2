import { Component, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from "@angular/router";
import { AuthService } from '../../core/auth/services/auth.service';

@Component({
  selector: 'app-login',
  imports: [RouterLink , ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
   private readonly fb = inject(FormBuilder);
    private readonly authService = inject(AuthService);
    private readonly router = inject(Router)
    loginForm : FormGroup = this.fb.group({
      email : ["" , [Validators.required , Validators.email]],
      password : ["" , [Validators.required ,Validators.pattern(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/)]]
})
    submitForm():void{
      if(this.loginForm.valid){
        this.authService.signIn(this.loginForm.value).subscribe({
          next:(res)=> {
            if(res.message === 'success'){
                if(res.user.role === 'admin'){
                    this.router.navigate(['/content']);
                }else{
                  this.router.navigate(['/home']);
                }
              localStorage.setItem('RefreshToken' , res.refreshToken)
              localStorage.setItem('AccessToken' , res.token)
              localStorage.setItem('userToken' ,JSON.stringify(res.user))
              this.authService.isLogged.set(true);
            }
          },
        })
      }else(
        this.loginForm.markAllAsTouched()
      )
    }
}
