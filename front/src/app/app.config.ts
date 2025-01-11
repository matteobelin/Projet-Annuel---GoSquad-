import { provideRouter, RouterModule } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { HomeComponent } from './home/home.component';
import { provideHttpClient } from '@angular/common/http';

export const appConfig = {
  providers: [
    provideHttpClient(),
    provideRouter([
      { path: 'home', component: HomeComponent },
      // Vous pouvez ajouter ici d'autres routes
    ]),
    provideAnimations()  // Pour les animations de routage (si nécessaire)
  ],
};
