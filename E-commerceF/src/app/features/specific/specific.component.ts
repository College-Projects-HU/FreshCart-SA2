import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { BrandsService } from '../../core/services/brands.service';
import { Product } from '../../core/models/product.interface';
import { SpecificBrand } from '../../core/models/specific-brand.interface';
import { ToastrService } from 'ngx-toastr';
import { CartService } from '../../core/services/cart.service';
import { WishlistService } from '../../core/services/wishlist.service';

@Component({
  selector: 'app-specific',
  imports: [RouterLink],
  templateUrl: './specific.component.html',
  styleUrl: './specific.component.css',
})
export class SpecificComponent implements OnInit {
  private readonly activateRoute = inject(ActivatedRoute);
  private readonly brandsService = inject(BrandsService);
  private readonly toastrService = inject(ToastrService);
  private readonly cartService = inject(CartService);
  private readonly wishlistService = inject(WishlistService);
  wishlistIds = computed(() => this.wishlistService.wishlistIds());
  brandDetails = signal<SpecificBrand>({} as SpecificBrand)
  productList = signal<Product[]>([])
   ngOnInit(): void {
    this.activateRoute.paramMap.subscribe((params)=>{
      this.getBrandDetails(params.get('id')!);
      this.getBrandProductDetails(params.get('id')!)
    });
    // this.brandDetails.set(res.data)
  }
  getBrandDetails(id: string):void {
  this.brandsService.getSpecificBrand(id).subscribe({
     next:(res)=>{
        this.brandDetails.set(res.data)
      },
      error:(err)=>{
        console.log(err)
      },
  })
 }
  getBrandProductDetails(id: string):void {
  this.brandsService.getProductsByBrand(id).subscribe({
     next:(res)=>{
        this.productList.set(res.data)
      },
      error:(err)=>{
        console.log(err)
      },
  })
 }
 addToCart(id:string):void{
   if(localStorage.getItem('freshToken')){
     this.cartService.addProductToCart(id).subscribe({
      next:(res)=>{
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
