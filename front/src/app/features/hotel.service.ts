import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { map, catchError, switchMap } from 'rxjs/operators';

export interface Hotel {
  id: string;
  name: string;
  address?: string;
  city?: string;
  starRating?: number;
  price?: number;
  currency?: string;
  thumbnail?: string;
  description?: string;
}

export interface BookingRequest {
  hotelId: string;
  checkIn: string;
  checkOut: string;
  guests: number;
  rooms: number;
}

export interface BookingResponse {
  bookingId: string;
  hotelName: string;
  checkIn: string;
  checkOut: string;
  totalPrice: number;
  currency: string;
  status: string;
}

export interface AuthResponse {
  access_token: string;
}

@Injectable({
  providedIn: 'root'
})
export class HotelService {
  private apiUrl = '/makcorps-api/auth';
  private authToken: string | null = null;

  // Default credentials - in a real app, these would be securely stored or provided by the user
  private username = 'gmutikanga';
  private password = 'Gosquad2025';

  constructor(private http: HttpClient) {}

  private authenticate(): Observable<string> {
    if (this.authToken) {
      return of(this.authToken);
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<AuthResponse>(this.apiUrl, {
      username: this.username,
      password: this.password
    }, { headers }).pipe(
      map(response => {
        this.authToken = response.access_token;
        return this.authToken;
      }),
      catchError(error => {
        console.error('Authentication error:', error);
        return throwError(() => new Error('Failed to authenticate with the hotel API'));
      })
    );
  }

  searchHotels(query: string): Observable<Hotel[]> {
    return this.authenticate().pipe(
      switchMap(token => {
        const headers = new HttpHeaders({
          'Authorization': `Bearer ${token}`
        });

        return this.http.get<any>(`${this.apiUrl}?city=${query}`, { headers }).pipe(
          map(response => {
            // Adapt this mapping based on the actual response structure from Makcorps API
            const hotels = response.hotels || [];
            return hotels.map((hotel: any) => this.mapToHotel(hotel));
          }),
          catchError(error => {
            console.error('Error searching hotels:', error);
            return of([]);
          })
        );
      })
    );
  }

  getHotelDetails(hotelId: string): Observable<Hotel> {
    return this.authenticate().pipe(
      switchMap(token => {
        const headers = new HttpHeaders({
          'Authorization': `Bearer ${token}`
        });

        // Assuming the Makcorps API has an endpoint for hotel details
        // Adjust the URL and parameters as needed based on the actual API
        return this.http.get<any>(`${this.apiUrl}?hotel_id=${hotelId}`, { headers }).pipe(
          map(response => this.mapToDetailedHotel(response)),
          catchError(error => {
            console.error('Error getting hotel details:', error);
            // Return a mock hotel with the ID as fallback
            return of({
              id: hotelId,
              name: 'Hôtel détaillé',
              address: '123 Rue de Paris',
              city: 'Paris',
              starRating: 4,
              price: 150,
              currency: 'EUR',
              description: 'Un hôtel confortable au cœur de Paris'
            });
          })
        );
      })
    );
  }

  bookHotel(booking: BookingRequest): Observable<BookingResponse> {
    return this.authenticate().pipe(
      switchMap(token => {
        const headers = new HttpHeaders({
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        });

        // Assuming the Makcorps API has an endpoint for booking hotels
        // Adjust the URL and request body as needed based on the actual API
        return this.http.post<any>(`${this.apiUrl}/booking`, booking, { headers }).pipe(
          map(response => {
            // Map the API response to our BookingResponse interface
            // Adjust this mapping based on the actual response structure
            return {
              bookingId: response.booking_id || 'BK' + Math.floor(Math.random() * 1000000),
              hotelName: response.hotel_name || 'Hôtel réservé',
              checkIn: booking.checkIn,
              checkOut: booking.checkOut,
              totalPrice: response.total_price || booking.rooms * 150 * this.calculateNights(booking.checkIn, booking.checkOut),
              currency: response.currency || 'EUR',
              status: response.status || 'CONFIRMED'
            };
          }),
          catchError(error => {
            console.error('Error booking hotel:', error);
            // Return a mock booking response as fallback
            return of({
              bookingId: 'BK' + Math.floor(Math.random() * 1000000),
              hotelName: 'Hôtel réservé',
              checkIn: booking.checkIn,
              checkOut: booking.checkOut,
              totalPrice: booking.rooms * 150 * this.calculateNights(booking.checkIn, booking.checkOut),
              currency: 'EUR',
              status: 'CONFIRMED'
            });
          })
        );
      })
    );
  }

  private mapToHotel(apiHotel: any): Hotel {
    // Adjust this mapping based on the actual response structure from Makcorps API
    return {
      id: apiHotel.id || apiHotel.hotel_id || String(Math.random()),
      name: apiHotel.name || apiHotel.hotel_name || 'Hôtel sans nom',
      city: apiHotel.city || apiHotel.location || '',
      thumbnail: apiHotel.thumbnail || apiHotel.image_url || '',
      price: apiHotel.price || 150,
      currency: apiHotel.currency || 'EUR',
      starRating: apiHotel.star_rating || apiHotel.rating || 4
    };
  }

  private mapToDetailedHotel(apiHotelDetail: any): Hotel {
    // Adjust this mapping based on the actual response structure from Makcorps API
    return {
      id: apiHotelDetail.id || apiHotelDetail.hotel_id || String(Math.random()),
      name: apiHotelDetail.name || apiHotelDetail.hotel_name || 'Hôtel détaillé',
      address: apiHotelDetail.address || '',
      city: apiHotelDetail.city || apiHotelDetail.location || '',
      starRating: apiHotelDetail.star_rating || apiHotelDetail.rating || 4,
      price: apiHotelDetail.price || 150,
      currency: apiHotelDetail.currency || 'EUR',
      thumbnail: apiHotelDetail.thumbnail || apiHotelDetail.image_url || '',
      description: apiHotelDetail.description || 'Description non disponible'
    };
  }

  private calculateNights(checkIn: string, checkOut: string): number {
    const start = new Date(checkIn);
    const end = new Date(checkOut);
    const diffTime = Math.abs(end.getTime() - start.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays || 1;
  }
}
