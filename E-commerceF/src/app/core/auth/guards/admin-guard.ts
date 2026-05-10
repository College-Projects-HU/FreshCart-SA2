import { isPlatformBrowser } from '@angular/common';
import { inject, PLATFORM_ID } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const adminGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);

  if (isPlatformBrowser(platformId)) {

    const token = localStorage.getItem('AccessToken');

    if (!token) {
      return router.parseUrl('/login');
    }

    try {
     
      const payload = JSON.parse(atob(token.split('.')[1]));

      const role = payload?.role;

      if (role === 'admin') {
        return true;
      }

      return router.parseUrl('/home');

    } catch (e) {
     
      return router.parseUrl('/login');
    }
  }

  return true;
};