import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class BrandsService {
  private readonly httpCliebt = inject(HttpClient);

  getAllBrands():Observable<any>{
    return this.httpCliebt.get(environment.baseUrl+'/api/v1/brands') //project
  }

  getSpecificBrand(brandId:string):Observable<any>{
    return this.httpCliebt.get(environment.baseUrl+`/api/v1/brands/${brandId}`)
  }
  getProductsByBrand(brandId: string): Observable<any> {
    return this.httpCliebt.get(`${environment.baseUrl}/api/v1/products?brand=${brandId}`);
  }
}
