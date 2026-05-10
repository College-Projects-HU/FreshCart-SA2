import { Component } from '@angular/core';

@Component({
  selector: 'app-footer-d',
  imports: [],
  templateUrl: './footer-d.component.html',
  styleUrl: './footer-d.component.css',
})
export class FooterDComponent {
  currentYear = new Date().getFullYear();
}
