import { isPlatformBrowser } from '@angular/common';
import { Inject, inject, PLATFORM_ID } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const id = inject(PLATFORM_ID);

  if(isPlatformBrowser(id)){
    if(localStorage.getItem('AccessToken')){
        return true;
  }else{
    return router.parseUrl('/login');
  }
  }else{
    return true;
  }
  
};
