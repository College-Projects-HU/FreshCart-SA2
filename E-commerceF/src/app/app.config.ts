import { ApplicationConfig, importProvidersFrom, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter, withInMemoryScrolling, withViewTransitions } from '@angular/router';
import { provideToastr } from 'ngx-toastr';
import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { errorInterceptor } from './core/interceptors/error-interceptor';
import { NgxSpinnerModule } from "ngx-spinner";
import { loadingInterceptor } from './core/interceptors/loading-interceptor';
import { headerInterceptor } from './core/interceptors/header-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes , withInMemoryScrolling({scrollPositionRestoration:"top"}), withViewTransitions()), provideClientHydration(withEventReplay()),
    provideHttpClient(withFetch(), withInterceptors([errorInterceptor , loadingInterceptor , headerInterceptor])),
    provideClientHydration(withEventReplay()),
    provideToastr(),
    importProvidersFrom(NgxSpinnerModule)
  ]
};
