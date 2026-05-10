import { Component, inject, signal } from '@angular/core';
import { CategoriesService } from '../../core/services/categories.service';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-subcategory',
  imports: [RouterLink],
  templateUrl: './subcategory.component.html',
  styleUrl: './subcategory.component.css',
})
export class SubcategoryComponent {
  private activatedRoute = inject(ActivatedRoute);
  private categoriesService = inject(CategoriesService);
  categoryName = signal<string>('');
  categoryImage = signal<string>('');
  categoryDetails = signal<any>(null);
  categoryId = signal<string | null>(null);
  subCategories = signal<any[]>([]);

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.categoryId.set(id);
        this.getCategoryInfo(id);
        this.getSubs(id);
      }
    });
  }

  getCategoryInfo(id: string) {
    this.categoriesService.getSpecificCategory(id).subscribe({
      next: (res) => {
        this.categoryDetails.set(res.data);
      },
    });
  }

  getSubs(id: string) {
    this.categoriesService.getSubCategoriesOnCategory(this.categoryId()!).subscribe({
      next: (res) => {
        this.subCategories.set(res.data);
      },
    });
  }
}
