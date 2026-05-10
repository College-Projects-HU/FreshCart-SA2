import { ProductService } from './../../core/services/product.service';
import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Product } from '../../core/models/product.interface';
import { ToastrService } from 'ngx-toastr';
import { CartService } from '../../core/services/cart.service';

@Component({
  selector: 'app-details',
  imports: [],
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css'],
})
export class DetailsComponent implements OnInit {
  private readonly activateRoute = inject(ActivatedRoute);
  private readonly productService = inject(ProductService);
  private readonly cartService = inject(CartService);
  private readonly toastrService = inject(ToastrService);
  productDetails = signal<Product>({} as Product)
  ngOnInit(): void {
    this.activateRoute.paramMap.subscribe((params)=>{
      this.getProductDetails(params.get('id')!);
    });
  }
  getProductDetails(id: string):void {
  this.productService.getSpecificProducts(id).subscribe({
     next:(res)=>{
        console.log(res);
        this.productDetails.set(res.data)
      },
      error:(err)=>{
        console.log(err)
      },
  })
 }
 addToCart(id:string):void{
    console.log(id);
   if(localStorage.getItem('AccessToken')){
     this.cartService.addProductToCart(id).subscribe({
      next:(res)=>{
        this.cartService.cartCount.set(res.numOfCartItems)
        this.toastrService.success(res.message , 'FreshCart' , {progressBar:true , closeButton:true})
      },
    })
   }else{
      this.toastrService.warning("Login first" , 'FreshCart' , {progressBar:true , closeButton:true})
   }
  }
}


 

