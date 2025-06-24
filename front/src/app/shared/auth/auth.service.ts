import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {Store} from '@ngrx/store';
import {AppState} from '../../store/app.state';
import {setAdvisorById} from '../../store/advisor/advisor.actions';
import {TokenService} from '../../core/services/token.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private loggedIn = false;

  constructor(
    private http: HttpClient,
    private store: Store<AppState>,
    private tokenService: TokenService
  ) {
    this.loggedIn = !!localStorage.getItem('authToken');
  }

  /**
   * Checks if the user is logged in.
   * @param credentials - The user's credentials.
   * @returns Observable<boolean> indicating login status.
   */
  login(credentials: { email: string, password: string, companyCode: string }): Observable<{ success: boolean, message?: string }> {
    return this.http.post('http://localhost:8080/login', credentials, { observe: 'response', responseType: 'text' }).pipe(
      map(response => {
        if (response.body === 'Connexion réussie') {
          const authToken = response.headers.get('Authorization');
          if (authToken) {
            this.loggedIn = true;
            localStorage.setItem('authToken', authToken);

            // Récupérer l'ID de l'advisor depuis le token et dispatcher l'action
            const advisorData = this.tokenService.getAdvisorFromToken();
            if (advisorData) {
              this.store.dispatch(setAdvisorById({ id: advisorData.id }));
            }

            return { success: true };
          } else {
            console.warn('Login successful but no Authorization token found in headers. Setting loggedIn to false.');
            this.loggedIn = false;
            return { success: false, message: 'Login successful, but token missing from headers.' };
          }
        } else {
          this.loggedIn = false;
          return { success: false, message: response.body || 'Unexpected response from server.' };
        }
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('HTTP Error during login:', error);
        this.loggedIn = false;
        let errorMessage = 'An error occurred during login.';

        // Special handling for parsing errors on 200 OK responses where Content-Type is application/json but body is text
        if (error.status === 200 && error.error instanceof SyntaxError) {
          const rawBodyText = error.message.match(/Unexpected token '[^,]+', "([^"]+)"/)?.[1];
          if (rawBodyText === 'Connexion réussie') {
            const authToken = error.headers.get('Authorization');
            if (authToken) {
              this.loggedIn = true;
              localStorage.setItem('authToken', authToken);

              // Récupérer l'ID de l'advisor depuis le token et dispatcher l'action
              const advisorData = this.tokenService.getAdvisorFromToken();
              if (advisorData) {
                this.store.dispatch(setAdvisorById({ id: advisorData.id }));
              }

              return throwError(() => ({ success: true, message: 'Login successful (backend returned non-JSON text with JSON Content-Type).' }));
            } else {
              errorMessage = 'Login successful, but token missing from headers during parsing error.';
            }
          } else {
            errorMessage = `Server sent invalid JSON: "${rawBodyText || error.message}"`;
          }
        } else if (error.status === 401 || error.status === 400) {
          // Standard HTTP errors for invalid credentials or bad requests
          if (typeof error.error === 'string') {
            errorMessage = error.error;
          } else if (error.error && typeof error.error === 'object' && error.error.message) {
            errorMessage = error.error.message;
          } else {
            errorMessage = 'Invalid email, password, or company code.';
          }
        } else {
          errorMessage = `Network or server error: ${error.message}`;
        }
        return throwError(() => ({ success: false, message: errorMessage }));
      })
    );
  }


  /**
   * Logs out the user by clearing the authentication token and refreshing the app.
   */
  logout(): void {
    this.loggedIn = false;
    localStorage.removeItem('authToken');
    // refresh app
    window.location.reload();
  }

  /**
   * Checks if the user is currently logged in.
   * @returns boolean indicating whether the user is logged in.
   */
  isLoggedIn(): boolean {
    return this.loggedIn;
  }
}
