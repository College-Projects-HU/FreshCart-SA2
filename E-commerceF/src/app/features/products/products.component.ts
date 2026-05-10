import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { Product } from '../../core/models/product.interface';
import { Category } from '../../core/models/category.interface';
import { ProductService } from '../../core/services/product.service';
import { CategoriesService } from '../../core/services/categories.service';
import { RouterLink } from "@angular/router";
import { AdminService } from '../../core/services/admin.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-products',
  imports: [RouterLink],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css',
})
export class ProductsComponent implements OnInit {
  private readonly productService    = inject(ProductService);
  private readonly adminService      = inject(AdminService);
  private readonly toastrService     = inject(ToastrService);
  private readonly categoriesService = inject(CategoriesService);

  productList    = signal<Product[]>([]);
  categoriesList = signal<Category[]>([]);

  // 1. signals للسيرش والفيلتر
  searchQuery        = signal<string>('');
  selectedCategory   = signal<string>('');

  // 2. computed بيتحدث أوتوماتيك
  filteredProducts = computed(() => {
    const query    = this.searchQuery().toLowerCase().trim();
    const category = this.selectedCategory();

    return this.productList().filter(product => {
      const matchesSearch =
        !query ||
        product.title.toLowerCase().includes(query)         ||
        product.brand?.name.toLowerCase().includes(query)   ||
        product.category?.name.toLowerCase().includes(query);

      const matchesCategory =
        !category ||
        product.category?._id === category;

      return matchesSearch && matchesCategory;
    });
  });

  ngOnInit(): void {
    this.getProductsData();
    this.getAllCategories();
  }

  // 3. methods بتحدث الـ signals
  onSearch(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchQuery.set(input.value);
  }

  onCategoryFilter(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.selectedCategory.set(select.value);
  }

  getProductsData(): void {
    this.productService.getAllProducts().subscribe({
      next: (res) => {
        this.productList.set(res.data);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  getAllCategories(): void {
    this.categoriesService.getAllCategories().subscribe({
      next: (res) => {
        this.categoriesList.set(res.data);
      },
    });
  }

  formatDateToShort(dateString: string | Date): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { month: 'short', day: '2-digit', year: 'numeric' });
  }

  formatTime(dateString: string | Date): string {
    const date = new Date(dateString);
    return date.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit', hour12: true });
  }

  deleteProduct(id: string): void {
    this.adminService.deleteProduct(id).subscribe({
      next: () => {
        this.toastrService.success('Product Removed', 'FreshCart', { progressBar: true, closeButton: true });
        this.productList.update(products => products.filter(product => product.id !== id));
      },
    });
  }
}