import { Component, computed, inject, OnInit, PLATFORM_ID, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { WishlistService } from '../../core/services/wishlist.service';
import { CartService } from '../../core/services/cart.service';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-wishlist',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './wishlist.component.html',
  styleUrl: './wishlist.component.css',
})
export class WishlistComponent implements OnInit {
  private readonly wishlistService = inject(WishlistService);
  private readonly cartService = inject(CartService);
  private readonly platformId = inject(PLATFORM_ID);

  wishlistItems = signal<any[]>([]);
  cartProductIds = signal<Set<string>>(new Set());
  isLoading = signal(true);
  actionLoadingId = signal<string | null>(null);

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadInitialData();
    }
  }

  loadInitialData(): void {
    this.wishlistService
      .getWishlistItems()
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (res: any) => {
          this.wishlistItems.set(res.data);
          this.wishlistService.wishCount.set(res.data.length);
          const ids = res.data.map((item: any) => item._id);
          this.wishlistService.wishlistIds.set(ids);
        },
      });
  }

  addToCart(productId: string) {
    this.actionLoadingId.set(productId);
    this.cartService
      .addProductToCart(productId)
      .pipe(finalize(() => this.actionLoadingId.set(null)))
      .subscribe({
        next: (res) => {
          this.cartService.cartCount.set(res.numOfCartItems);
          this.cartProductIds.update((prev) => new Set(prev).add(productId));
        },
      });
  }

  removeProduct(productId: string) {
    this.actionLoadingId.set(productId);
    this.wishlistService
      .removeProductFromWishlist(productId)
      .pipe(finalize(() => this.actionLoadingId.set(null)))
      .subscribe({
        next: () => {
          this.wishlistItems.update((items) => items.filter((p) => p._id !== productId));
          this.wishlistService.wishCount.set(this.wishlistItems().length);
        },
      });
  }
}
