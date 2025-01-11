import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',  // Ce service est disponible pour toute l'application
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/hello';  // Remplacez avec votre propre API

  constructor(private http: HttpClient) {}

  getHelloMessage(): Observable<{ message: string }> {
    return this.http.get<{ message: string }>(this.apiUrl);
  }

}
