import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WishlistService {
  private readonly httpClient = inject(HttpClient);
  wishCount = signal<number>(0);
  wishlistIds = signal<string[]>([]);

  addProuctToWishlist(productId: string) {
    return this.httpClient.post(`${environment.baseUrl}/api/v1/wishlist`, { productId: productId });
  }

  getWishlistItems() {
    return this.httpClient.get(`${environment.baseUrl}/api/v1/wishlist`);
  }

  removeProductFromWishlist(productId: string) {
    return this.httpClient.delete(`${environment.baseUrl}/api/v1/wishlist/${productId}`);
  }

  getLoggedUserWishlist(): Observable<any> {
    return this.httpClient.get(`${environment.baseUrl}/api/v1/wishlist`);
  }
}
