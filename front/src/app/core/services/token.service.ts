import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

/**
 * Service to handle JWT token operations such as decoding and validation.
 */
export interface DecodedToken {
  sub: string;
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
  exp: number;
  iat: number;
  companyCode: string;
}

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  // =========================================================== //
  // ========================= METHODS ========================= //
  // =========================================================== //
  /**
   * Retrieves the JWT token from localStorage.
   * @returns The JWT token as a string or null if not found.
   */
  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  /**
   * Decodes the JWT token and returns its payload.
   * @returns The decoded token as an object or null if decoding fails.
   */
  decodeToken(): DecodedToken | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      return jwtDecode<DecodedToken>(token);
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  }

  /**
   * Retrieves the advisor information from the decoded token.
   * @returns An object containing advisor details or null if token is invalid.
   */
  getAdvisorFromToken(): { id: number; firstName: string; lastName: string; email: string, companyCode:string } | null {
    const decodedToken = this.decodeToken();
    if (!decodedToken) return null;

    return {
      email: '',
      firstName: '',
      lastName: '',
      id: parseInt(decodedToken.sub, 10),
      companyCode: decodedToken.companyCode
    };
  }
}
