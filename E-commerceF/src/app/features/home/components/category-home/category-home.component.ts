
import { Component, inject, OnInit, signal } from '@angular/core';
import { CategoriesService } from '../../../../core/services/categories.service';
import { Category } from '../../../../core/models/category.interface';
import { TwoStaticComponent } from "./components/two-static/two-static.component";

@Component({
  selector: 'app-category-home',
  imports: [TwoStaticComponent],
  templateUrl: './category-home.component.html',
  styleUrl: './category-home.component.css',
})
export class CategoryHomeComponent implements OnInit {
  private readonly categoriesService = inject(CategoriesService);
  
  categoriesList = signal<Category[]>([])
  ngOnInit(): void {
    this.getCategoriesData();
  }

  getCategoriesData():void {
    this.categoriesService.getAllCategories().subscribe({
      next:(res)=> {
        this.categoriesList.set(res.data)
      },
      error:(err)=>{
        console.log(err)
      }
    })
  }
}
