import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../core/services/product.service';
import { AdminService } from '../../core/services/admin.service';
import { Brands } from '../../core/models/brands.interface';
import { Category, SubCategory } from '../../core/models/product.interface';
import { CategoriesService } from '../../core/services/categories.service';
import { BrandsService } from '../../core/services/brands.service';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-add-product',
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.css',
})
export class AddProductComponent implements OnInit {

  private readonly fb           = inject(FormBuilder);
  private readonly router       = inject(Router);
  private readonly adminService = inject(AdminService);
  private readonly categoriesService = inject(CategoriesService);
  private readonly brandsService = inject(BrandsService);
  private readonly cdr          = inject(ChangeDetectorRef); 
  private readonly toastrService          = inject(ToastrService); 

  productForm!: FormGroup;
  isSubmitting      = false;  
  loadingCategories = false;
  loadingBrands     = false;
  loadingSubcats    = false;
  categories:    Category[]    = [];
  brands:        Brands[]      = [];
  subcategories: SubCategory[] = [];
  coverPreview  = '';
  imagePreviews: string[] = [];
  isDragging    = false;

  ngOnInit(): void {
    this.buildForm();
    this.loadCategories();
    this.loadBrands();
  }

  private buildForm(): void {
    this.productForm = this.fb.group({
      title:          ['', [Validators.required, Validators.minLength(3)]],
      slug:           [''],
      description:    ['', [Validators.required, Validators.minLength(20)]],
      price:          [null, [Validators.required, Validators.min(0)]],
      quantity:       [null, [Validators.required, Validators.min(0)]],
      imageCover:     ['', Validators.required],
      images:         [[]],
      CategoryId:     ['', Validators.required],
      SubcategoryIds: [[]],
      BrandId:        [''],
    });

    this.productForm.get('title')?.valueChanges.subscribe((val: string) => {
      const slug = val?.toLowerCase().replace(/\s+/g, '-').replace(/[^a-z0-9-]/g, '') || '';
      this.productForm.get('slug')?.setValue(slug, { emitEvent: false });
    });
  }

  get fc() { return this.productForm.controls; }

  private loadCategories(): void {
    this.loadingCategories = true;
    this.categoriesService.getAllCategories().subscribe({
      next: (res) => {
        this.categories = res.data ?? res;
        this.loadingCategories = false;
        this.cdr.detectChanges(); 
      },
      error: () => { this.loadingCategories = false; },
    });
  }

  private loadBrands(): void {
    this.loadingBrands = true;
    this.brandsService.getAllBrands().subscribe({
      next: (res) => {
        this.brands = res.data ?? res;
        this.loadingBrands = false;
        this.cdr.detectChanges();
      },
      error: () => { this.loadingBrands = false; },
    });
  }

  onCategoryChange(event: Event): void {
    const id = (event.target as HTMLSelectElement).value;
    this.productForm.get('CategoryId')?.setValue(id);

    this.subcategories = [];
    this.productForm.get('SubcategoryIds')?.setValue([]);

    if (!id) return;

    this.loadingSubcats = true;
    this.categoriesService.getSubCategoriesOnCategory(id).subscribe({
      next: (res) => {
        this.subcategories = res.data ?? res;
        this.loadingSubcats = false;
        this.cdr.detectChanges();
      },
      error: () => { this.loadingSubcats = false; },
    });
  }

  onSubcategoryChange(event: Event): void {
    const val = (event.target as HTMLSelectElement).value;
    this.productForm.get('SubcategoryIds')?.setValue(val ? [val] : []);
  }

  onCoverImageChange(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = (e) => {
      this.coverPreview = e.target?.result as string;
      this.productForm.get('imageCover')?.setValue(this.coverPreview);
      this.cdr.detectChanges();
    };
    reader.readAsDataURL(file);
  }


  onMultipleImagesChange(event: Event): void {
    const files = (event.target as HTMLInputElement).files;
    if (!files) return;
    this.readImageFiles(Array.from(files));
  }

  onDragOver(e: DragEvent): void  { e.preventDefault(); this.isDragging = true; }
  onDragLeave(): void             { this.isDragging = false; }
  onDrop(e: DragEvent): void {
    e.preventDefault();
    this.isDragging = false;
    const files = Array.from(e.dataTransfer?.files ?? []).filter(f => f.type.startsWith('image/'));
    this.readImageFiles(files);
  }

  private readImageFiles(files: File[]): void {
    files.forEach(file => {
      const reader = new FileReader();
      reader.onload = (e) => {
        const result = e.target?.result as string;
        this.imagePreviews.push(result);
        const current: string[] = this.productForm.get('images')?.value ?? [];
        this.productForm.get('images')?.setValue([...current, result]);
        this.cdr.detectChanges();
      };
      reader.readAsDataURL(file);
    });
  }

  removeImage(index: number): void {
    this.imagePreviews.splice(index, 1);
    const current: string[] = [...(this.productForm.get('images')?.value ?? [])];
    current.splice(index, 1);
    this.productForm.get('images')?.setValue(current);
  }

  onSubmit(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    const body = this.productForm.value;

    this.adminService.createProduct(body).subscribe({
      next: (res) => {
        this.isSubmitting = false;
        this.toastrService.success('Product Add' , 'FreshCart' , {progressBar:true , closeButton:true});
        this.router.navigate(['/products']);
      },
      error: (err) => {
        this.isSubmitting = false;
        this.toastrService.warning('Product not add' , 'FreshCart' , {progressBar:true , closeButton:true});
      },
    });
  }

  onReset(): void {
    this.productForm.reset({ images: [], SubcategoryIds: [] });
    this.imagePreviews = [];
    this.coverPreview  = '';
    this.subcategories = [];
  }
}