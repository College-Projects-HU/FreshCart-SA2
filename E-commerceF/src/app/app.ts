import { AuthService } from './core/auth/services/auth.service';
import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { ForgotComponent } from './features/forgot/forgot.component';
import { FooterComponent } from './features/footer/footer.component';
import { NgxSpinner, NgxSpinnerComponent } from 'ngx-spinner';
import { NavbarComponent } from './features/navbar/navbar.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet , NavbarComponent , FooterComponent,NgxSpinnerComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('E-commerce');
  private readonly authService = inject(AuthService)
  ngOnInit(): void {
  this.authService.initAuth();
}
}
