import { Component, inject, signal } from '@angular/core';
import { RouterLink } from "@angular/router";
import { Category, Product } from '../../core/models/product.interface';
import { CategoriesService } from '../../core/services/categories.service';
import { ProductService } from '../../core/services/product.service';
import { Brands } from '../../core/models/brands.interface';
import { BrandsService } from '../../core/services/brands.service';
import { Users } from '../../core/models/users.interface';
import { AdminService } from '../../core/services/admin.service';
import { AllOrders } from '../../core/models/all-orders.interface';

@Component({
  selector: 'app-content',
  imports: [RouterLink],
  templateUrl: './content.component.html',
  styleUrl: './content.component.css',
})
export class ContentComponent {
  private categoriesService = inject(CategoriesService);
  private readonly productService    = inject(ProductService);
  private readonly brandsService = inject(BrandsService);
  private readonly adminService = inject(AdminService);
  orderList = signal<AllOrders[]>([])
  usersList = signal<Users[]>([])
  categoriesList = signal<Category[]>([]);
  productList    = signal<Product[]>([]);
  brandList = signal<Brands[]>([])
  
  ngOnInit(): void {
    this.getProductsData();
    this.getAllCategories();
    this.getBrandsData();
    this.getAllOrders();
    this.getUsersData();
  }
  getAllCategories() {
    this.categoriesService.getAllCategories().subscribe({
      next: (res) => {
        this.categoriesList.set(res.data);
      },
    });
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
  getBrandsData():void{
    this.brandsService.getAllBrands().subscribe({
      next:(res)=>{
          this.brandList.set(res.data)
        
      },
      error:(err)=>{
        console.log(err)
      },
        })
  }
  getUsersData():void{
    this.adminService.getAllUsers().subscribe({
      next:(res)=>{
        console.log(res)
        this.usersList.set(res)
      },
      error:(err)=>{
        console.log(err)
      },
        })
  }
   getAllOrders():void{
    this.adminService.getAllOrders().subscribe({
      next:(res)=>{
        
        this.orderList.set(res.data)
      },
      error:(err)=>{
        console.log(err)
      },
        })
  }
  getTodayDate(): string {
  const today = new Date();

  return today.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
  }
}
