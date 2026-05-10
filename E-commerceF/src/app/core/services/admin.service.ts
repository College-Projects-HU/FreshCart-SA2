import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs'
import { environment } from '../../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class AdminService {
  private readonly httpClient = inject(HttpClient);

  
  getAllUsers():Observable<any>{
      return this.httpClient.get(`${environment.baseUrl}/api/v1/admin/getAllUsers`) //project
    }

  deleteUser(id:string):Observable<any>{
    return this.httpClient.delete(`${environment.baseUrl}/api/v1/admin/deleteUser/${id}`)//project
  }

  createProduct(data:object):Observable<any>{
    return this.httpClient.post(`${environment.baseUrl}/api/v2/admin/products` ,data)
  }

  updateProduct(productId:string , data:object):Observable<any>{
    return this.httpClient.put(`${environment.baseUrl}/api/v2/admin/products/${productId}` , data)
  }

  deleteProduct(id:string):Observable<any>{
    return this.httpClient.delete(`${environment.baseUrl}/api/v2/admin/products/${id}`)//project
  }

  getAllOrders(): Observable<any> {
    return this.httpClient.get(environment.baseUrl+`/api/v1/orders`);//project
  }
}
