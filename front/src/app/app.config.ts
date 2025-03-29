import {provideRouter} from '@angular/router';
import {provideAnimations} from '@angular/platform-browser/animations';
import {provideHttpClient} from '@angular/common/http';
import {routes} from './app.routes';

export const appConfig = {
  providers: [
    provideHttpClient(),
    provideRouter(routes),
    provideAnimations()
  ],
};
