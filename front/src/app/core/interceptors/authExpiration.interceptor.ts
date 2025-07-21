import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class TokenExpirationService {
  private checkIntervalMs = 60 * 1000; // Vérifier toutes les 60 secondes
  private intervalId: any;

  constructor(private router: Router) {}

  startTokenExpirationCheck(): void {
    this.checkTokenAndLogoutIfExpired(); // check immédiat au lancement
    this.intervalId = setInterval(() => {
      this.checkTokenAndLogoutIfExpired();
    }, this.checkIntervalMs);
  }

  stopTokenExpirationCheck(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  private checkTokenAndLogoutIfExpired(): void {
    const token = localStorage.getItem('authToken');
    if (token && this.isTokenExpired(token)) {
      console.log('Token expiré, suppression du token et déconnexion');
      localStorage.removeItem('authToken');
      this.router.navigate(['/login']);
    }
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const exp = payload.exp;
      if (!exp) return true; // pas de date d'expiration => considérer expiré
      const now = Math.floor(Date.now() / 1000);
      return now >= exp;
    } catch (e) {
      console.error('Erreur décodage token JWT:', e);
      return true; // token invalide = considéré expiré
    }
  }
}
