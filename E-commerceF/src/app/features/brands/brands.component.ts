import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from "@angular/router";
import { Product } from '../../core/models/product.interface';
import { CartService } from '../../core/services/cart.service';
import { BrandsService } from '../../core/services/brands.service';
import { Brands } from '../../core/models/brands.interface';

@Component({
  selector: 'app-brands',
  imports: [RouterLink],
  templateUrl: './brands.component.html',
  styleUrl: './brands.component.css',
})
export class BrandsComponent implements OnInit {
  private readonly brandsService = inject(BrandsService);
  brandList = signal<Brands[]>([])
  ngOnInit(): void {
    this.getBrandsData()
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
  getSpecificBrand(id:string):void{}
}
