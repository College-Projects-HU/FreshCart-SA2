import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AsideDComponent } from "../../features/aside-d/aside-d.component";
import { HeaderDComponent } from "../../features/header-d/header-d.component";
import { FooterDComponent } from "../../features/footer-d/footer-d.component";

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [RouterOutlet, AsideDComponent, HeaderDComponent, FooterDComponent],
  templateUrl: './admin-layout.component.html'
})
export class AdminLayoutComponent {}
