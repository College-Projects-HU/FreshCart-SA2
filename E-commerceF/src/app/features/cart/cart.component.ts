import { CartService } from './../../core/services/cart.service';
import { Component, inject, OnInit, PLATFORM_ID, signal } from '@angular/core';
import { Cart } from './models/cart.interface';
import { RouterLink } from "@angular/router";
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-cart',
  imports: [RouterLink],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css',
})
export class CartComponent implements OnInit {
  private readonly cartService = inject(CartService);
  private readonly id = inject(PLATFORM_ID);
  cartDetails = signal<Cart>({} as Cart)
  ngOnInit(): void {
    if(isPlatformBrowser(this.id)){
      this.getCartData()
    }
  }

  getCartData():void{
    this.cartService.getLoggedUserCart().subscribe({
      next:(res)=>{
        this.cartDetails.set(res.data)
      },
    })
  }

  removeItem(id:string):void{
    this.cartService.removeProductItem(id).subscribe({
      next:(res)=>{
        this.cartDetails.set(res.data);
        this.cartService.cartCount.set(res.numOfCartItems);
      },
    })
  }

  update(id:string , count:number):void{
    this.cartService.updateCartCount(id,count).subscribe({
      next:(res)=>{
        this.cartDetails.set(res.data);
      },
    })
  }

  clearAll():void{
    this.cartService.clearCart().subscribe({
      next:(res)=>{
        this.cartDetails.set(res.data);
        this.cartService.cartCount.set(0);
      },
    })
  }
}
