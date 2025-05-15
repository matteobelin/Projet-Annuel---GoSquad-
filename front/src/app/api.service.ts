import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private baseUrl = 'http://localhost:8080';  // Base URL for API

  constructor(private http: HttpClient) {}

  /**
   * Generic GET method
   * @param endpoint API endpoint
   * @param params Optional query parameters
   * @returns Observable of response
   */
  get<T>(endpoint: string, params?: any): Observable<T> {
    const options = { params: new HttpParams({ fromObject: params || {} }) };
    return this.http.get<T>(`${this.baseUrl}${endpoint}`, options);
  }

  /**
   * Generic POST method
   * @param endpoint API endpoint
   * @param body Request body
   * @returns Observable of response
   */
  post<T>(endpoint: string, body: any): Observable<T> {
    return this.http.post<T>(`${this.baseUrl}${endpoint}`, body);
  }

  /**
   * Generic PUT method
   * @param endpoint API endpoint
   * @param body Request body
   * @returns Observable of response
   */
  put<T>(endpoint: string, body: any): Observable<T> {
    return this.http.put<T>(`${this.baseUrl}${endpoint}`, body);
  }

  /**
   * Generic DELETE method
   * @param endpoint API endpoint
   * @returns Observable of response
   */
  delete<T>(endpoint: string): Observable<T> {
    return this.http.delete<T>(`${this.baseUrl}${endpoint}`);
  }
}
