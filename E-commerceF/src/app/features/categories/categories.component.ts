import { Component, inject, signal } from '@angular/core';
import { CategoriesService } from '../../core/services/categories.service';
import { Category } from '../../core/models/category.interface';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-categories',
  imports: [RouterLink],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css',
})
export class CategoriesComponent {
  private categoriesService = inject(CategoriesService);
  categoriesList = signal<Category[]>([]);

  ngOnInit(): void {
    this.getAllCategories();
  }

  getAllCategories() {
    this.categoriesService.getAllCategories().subscribe({
      next: (res) => {
        this.categoriesList.set(res.data);
      },
    });
  }
}