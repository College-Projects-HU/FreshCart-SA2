import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class OrdersService {
  subscribe(arg0: { next: (res: any) => void; }) {
    throw new Error('Method not implemented.');
  }
  private readonly httpClient = inject(HttpClient);

  getUserOrders(userId: string): Observable<any> {
    return this.httpClient.get(environment.baseUrl+`/api/v1/orders/user/${userId}`);
  }
  
}