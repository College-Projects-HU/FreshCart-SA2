import { isPlatformBrowser } from '@angular/common';
import { inject, PLATFORM_ID } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastrService = inject(ToastrService);
  const platformId = inject(PLATFORM_ID);

  return next(req).pipe(
    catchError((err) => {

      if (isPlatformBrowser(platformId)) {
        toastrService.error(
          err.error?.message || 'Error',
          'FreshCart',
          { progressBar: true, closeButton: true }
        );
      }
      return throwError(() => err);
    })
  );
};