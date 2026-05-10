import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from '../../features/footer/footer.component';
import { NavbarComponent } from '../../features/navbar/navbar.component';


@Component({
  selector: 'app-user-layout',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, FooterComponent],
  templateUrl: './user-layout.component.html'
})
export class UserLayoutComponent {}