import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SrvRecord } from 'dns';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private readonly httpClient = inject(HttpClient);
  
  cartCount = signal<number>(0);

  addProductToCart(prodId:string):Observable<any>{
   return this.httpClient.post(environment.baseUrl + `/api/v1/cart`, {
  productId: prodId,
}) //project
  }

  getLoggedUserCart():Observable<any>{
    return this.httpClient.get(environment.baseUrl + `/api/v1/cart`)
  }//project

  removeProductItem(Id:String):Observable<any>{
    return this.httpClient.delete(environment.baseUrl + `/api/v1/cart/${Id}`)
  }//project

  updateCartCount(id:string , count:number):Observable<any>{
    return this.httpClient.put(environment.baseUrl + `/api/v1/cart/${id}`, {
      "count": count,
    })//project
  }

  clearCart():Observable<any>{
    return this.httpClient.delete(environment.baseUrl + `/api/v1/cart`)
  }//project

  createCashOrder(cartId:string , data:object):Observable<any>{
    return this.httpClient.post(environment.baseUrl + `/api/v1/orders/${cartId}` ,data )
  }//project

  createVisaOrder(cartId:string , data:object):Observable<any>{
    return this.httpClient.post(environment.baseUrl + `/api/v1/orders/checkout-session/${cartId}?url=${environment.url}` ,data )
  }
}
