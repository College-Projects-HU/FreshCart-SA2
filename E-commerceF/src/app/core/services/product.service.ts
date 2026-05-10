import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly httpCliebt = inject(HttpClient);

  getAllProducts():Observable<any>{
    return this.httpCliebt.get(`${environment.baseUrl}/api/v1/products`)
  }

  getSpecificProducts(productId:string):Observable<any>{
    return this.httpCliebt.get(`${environment.baseUrl}/api/v1/products/${productId}`)
  }
}
