import { Component } from '@angular/core';
import { ContentComponent } from '../content/content.component';
import { AddProductComponent } from '../add-product/add-product.component';
import { ProductsComponent } from '../products/products.component';
import { UseresComponent } from '../useres/useres.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-aside-d',
  imports: [RouterModule],
  templateUrl: './aside-d.component.html',
  styleUrl: './aside-d.component.css',
})
export class AsideDComponent {
  sidebarOpen = false;
  closeSidebar(): void {
    this.sidebarOpen = false;
    document.body.classList.remove('overflow-hidden');
  }
  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
    document.body.classList.toggle('overflow-hidden', this.sidebarOpen);
  }
}
