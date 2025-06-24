import { HttpInterceptorFn } from '@angular/common/http';

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('authToken');

  if (token) {
    // Remove any existing Bearer prefix if present
    const cleanToken = token.replace(/^Bearer\s+/i, '');
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${cleanToken}`
      }
    });
  }

  return next(req);
};
