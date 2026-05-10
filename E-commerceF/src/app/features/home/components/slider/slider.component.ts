import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { StaticItemsComponent } from "./components/static-items/static-items.component";

@Component({
  selector: 'app-slider',
  imports: [StaticItemsComponent],
  templateUrl: './slider.component.html',
  styleUrl: './slider.component.css',
  schemas:[CUSTOM_ELEMENTS_SCHEMA]
})
export class SliderComponent {}
