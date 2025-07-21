import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('authToken');

  if (token) {
    const cleanToken = token.replace(/^Bearer\s+/i, '');
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${cleanToken}`
      }
    });
  }

  return next(req).pipe(
    catchError(error => {
      if (error.status === 401) {
        console.warn('Token invalide ou expiré : déconnexion');
        localStorage.removeItem('authToken');
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
