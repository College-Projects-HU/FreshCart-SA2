import { Component } from '@angular/core';
import { SliderComponent } from './components/slider/slider.component';
import { ProductComponent } from './components/product/product.component';
import { CategoryHomeComponent } from './components/category-home/category-home.component';

@Component({
  selector: 'app-home',
  imports: [SliderComponent,ProductComponent,CategoryHomeComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {}
