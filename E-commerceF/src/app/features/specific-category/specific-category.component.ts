import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CategoriesService } from '../../core/services/categories.service';

@Component({
  selector: 'app-specific-category',
  imports: [RouterLink],
  templateUrl: './specific-category.component.html',
  styleUrl: './specific-category.component.css',
})
export class SpecificCategoryComponent {
  private activatedRoute = inject(ActivatedRoute);
  private categoriesService = inject(CategoriesService);

  specificSubId = signal<string | null>(null);
  specificSubDetails = signal<any>(null);
  productsList = signal<any[]>([]);
  isLoading = signal<boolean>(true);

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.specificSubId.set(id);
        this.getSpecificSubDetails(id);
      }
    });
  }

  getSpecificSubDetails(id: string) {
    this.isLoading.set(true);

    this.categoriesService.getSpecificSubCategoriesOnCategory(id).subscribe({
      next: (res) => {
        this.specificSubDetails.set(res.data);

        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error fetching specific subcategory:', err);
        this.isLoading.set(false);
      },
    });
  }
}
