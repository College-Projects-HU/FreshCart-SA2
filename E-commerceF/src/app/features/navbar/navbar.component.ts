import { AuthService } from './../../core/auth/services/auth.service';
import { Component, computed, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { RouterLink, RouterLinkActive } from "@angular/router";
import { initFlowbite } from 'flowbite/lib/esm/components';
import { FlowbiteService } from '../../core/services/flowbite.service';
import { isPlatformBrowser } from '@angular/common';
import { CartService } from '../../core/services/cart.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent implements OnInit {
  private readonly authService = inject(AuthService)
  private readonly cartService = inject(CartService)
  private readonly pLATFORM_ID = inject(PLATFORM_ID);
  isOpen = false;


  logged = computed(()=>this.authService.isLogged());


  count = computed(()=> this.cartService.cartCount())
  constructor(private flowbiteService: FlowbiteService) {}
  nameU:string ="";
  emailU:string ="";

  ngOnInit(): void {

  if (isPlatformBrowser(this.pLATFORM_ID)) {

    const userToken = localStorage.getItem('userToken');

    if (userToken) {
      try {
        const user = JSON.parse(userToken);
        this.nameU = user?.name ?? '';
        this.emailU = user?.email ?? '';
      } catch (e) {
        console.log('Invalid userToken');
      }
    }

    this.getCartCount();

    const freshToken = localStorage.getItem('freshToken');
    if (freshToken) {
      this.authService.isLogged.set(true);
    }
  }

  this.flowbiteService.loadFlowbite(() => {
    initFlowbite();
  });
}

  logOut():void{
    this.authService.signOut();
  }

  getCartCount():void{
    this.cartService.getLoggedUserCart().subscribe({
      next:(res)=> {
        this.cartService.cartCount.set(res.numOfCartItems)
      },
    })
  }

  toggleMenu() {
  this.isOpen = !this.isOpen;
}
}
