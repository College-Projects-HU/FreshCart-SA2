import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Inject, inject, Injectable, PLATFORM_ID, signal } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly httpClient = inject(HttpClient);
  private readonly router = inject(Router)
  private readonly platformId = inject(PLATFORM_ID);
  isLogged = signal<boolean>(false);
  private isBrowser(): boolean {
    return isPlatformBrowser(this.platformId);
  }
  
  getCurrentUserId(): string {
  const userData = localStorage.getItem('userToken');

  if (!userData) return '';

  try {
    const parsedUser = JSON.parse(userData);
    return parsedUser.id?.toString() || '';
  } catch (error) {
    return '';
  }
}

  signOut(): void {
    if (this.isBrowser()) {
      localStorage.removeItem('AccessToken');
      localStorage.removeItem('RefreshToken');
      localStorage.removeItem('userToken');
      this.isLogged.set(false);
    this.router.navigate(['/login'])
    }
  }
  
  refreshToken() {
    const refreshToken = this.isBrowser()
      ? localStorage.getItem('RefreshToken')
      : null;

    return this.httpClient.post(
      'http://localhost:8081/api/v1/auth/refresh',
      {},
      {
        headers: {
          'X-Refresh-Token': refreshToken || ''
        }
      }
    );
  }

  initAuth(): void {
  if (isPlatformBrowser(this.platformId)) {
    const token = localStorage.getItem('AccessToken');
    this.isLogged.set(!!token);
  } else {
    this.isLogged.set(false);
  }
}

  signUp(data:object):Observable<any>{
    return this.httpClient.post(environment.baseUrl + `/api/v1/auth/signup` , data) // PROJECT
  }

  signIn(data:object):Observable<any>{
    return this.httpClient.post( environment.baseUrl + `/api/v1/auth/signin` , data) // PROJECT
  }

  forgotPassword(data:object):Observable<any>{
    return this.httpClient.post(environment.baseUrl + `/api/v1/auth/forgotPasswords` , data) // PROJECT
  }

  verifyCode(data:object):Observable<any>{
    return this.httpClient.post(environment.baseUrl + `/api/v1/auth/verifyResetCode` , data) // PROJECT
  }

  resetPassword(data:object):Observable<any>{
    return this.httpClient.put(environment.baseUrl + `/api/v1/auth/resetPassword` , data) // PROJECT
  }

  changePassword(data:Object):Observable<any>{
    return this.httpClient.put(environment.baseUrl +`/api/v1/users/changeMyPassword` , data  );
  }

}
