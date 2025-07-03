import { ApplicationConfig, APP_INITIALIZER } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import { routes } from './app.routes';
import { provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { appReducer } from './store/reducers/app.reducer';
import { AppEffects } from './store/effects/app.effects';
import { advisorReducer } from './store/advisor/advisor.reducer';
import { AdvisorEffects } from './store/advisor/advisor.effects';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import { isDevMode } from '@angular/core';
import {AuthInterceptor} from './core/interceptors/auth.interceptor';
import { AppInitService } from './core/services/app-init.service';
import { customerReducer } from './store/customer/customer.reducer';
import { CustomerEffects } from './store/customer/customer.effects';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withInterceptors([AuthInterceptor])
    ),
    provideRouter(routes),
    provideAnimations(),
    provideStore({
      Gosquad: appReducer,
      advisor: advisorReducer,
      customer: customerReducer
    }),
    provideEffects([AppEffects, AdvisorEffects,CustomerEffects ]),
    provideStoreDevtools({
      maxAge: 25,
      logOnly: !isDevMode(),
      autoPause: true,
    }),
    {
      provide: APP_INITIALIZER,
      useFactory: (appInitService: AppInitService) => () => appInitService.initializeApp(),
      deps: [AppInitService],
      multi: true
    }
  ],
};
