import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class FlightService {
  private apiUrl = 'http://localhost:8080/flights';

  constructor(private http: HttpClient) {}

  searchFlights(from: string, to: string): Observable<any> {
    return this.http.get<any>(this.apiUrl, {
      params: {
        dep_iata: from,
        arr_iata: to
      }
    });
  }
}
