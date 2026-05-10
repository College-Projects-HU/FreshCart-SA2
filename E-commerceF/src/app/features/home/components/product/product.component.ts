import { ToastrService } from 'ngx-toastr';
import { Product } from '../../../../core/models/product.interface';
import { CartService } from '../../../../core/services/cart.service';
import { ProductService } from './../../../../core/services/product.service';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from "@angular/router";
import { WishlistService } from '../../../../core/services/wishlist.service';

@Component({
  selector: 'app-product',
  imports: [RouterLink],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css',
})
export class ProductComponent implements OnInit {
  
  private readonly productService = inject(ProductService);
  private readonly cartService = inject(CartService);
  private readonly wishlistService = inject(WishlistService);
  private readonly toastrService = inject(ToastrService);
  wishlistIds = computed(() => this.wishlistService.wishlistIds());
  
  productList = signal<Product[]>([])
  ngOnInit(): void {
    this.getProductsData()
  }

  getProductsData():void{
    this.productService.getAllProducts().subscribe({
      next:(res)=>{
        this.productList.set(res.data)
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
  addToWishlist(id: string): void {
    const isInWishlist = this.wishlistIds().includes(id);

    if (isInWishlist) {
      this.wishlistService.removeProductFromWishlist(id).subscribe({
        next: (res: any) => {
          this.wishlistService.wishlistIds.set([...res.data]);
          this.wishlistService.wishCount.set(res.data.length);
        },
      });
    } else {
      this.wishlistService.addProuctToWishlist(id).subscribe({
        next: (res: any) => {
          this.wishlistService.wishlistIds.set([...res.data]);
          this.wishlistService.wishCount.set(res.data.length);
        },
      });
    }
  }
}
