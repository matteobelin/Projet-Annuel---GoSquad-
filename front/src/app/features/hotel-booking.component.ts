import { Component } from '@angular/core';
import { HotelService, Hotel, BookingRequest, BookingResponse } from './hotel.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgForOf, NgIf } from '@angular/common';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-hotel-booking',
  templateUrl: './hotel-booking.component.html',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgIf,
    NgForOf
  ],
  styleUrls: ['./hotel-booking.component.css']
})
export class HotelBookingComponent {
  hotels: Hotel[] = [];
  query: string = '';
  loading = false;
  error = '';

  // Selected hotel for details view
  selectedHotel: Hotel | null = null;
  loadingDetails = false;

  // Booking related properties
  bookingInProgress = false;
  bookingForm: BookingRequest = {
    hotelId: '',
    checkIn: this.formatDate(this.addDays(new Date(), 1)),
    checkOut: this.formatDate(this.addDays(new Date(), 3)),
    guests: 2,
    rooms: 1
  };
  bookingConfirmation: BookingResponse | null = null;

  constructor(private hotelService: HotelService) {}

  searchHotels() {
    if (!this.query.trim()) {
      this.error = 'Veuillez entrer une destination';
      return;
    }

    this.loading = true;
    this.error = '';
    this.hotels = [];
    this.selectedHotel = null;
    this.bookingConfirmation = null;

    this.hotelService.searchHotels(this.query)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (hotels) => {
          this.hotels = hotels;
          if (hotels.length === 0) {
            this.error = 'Aucun hôtel trouvé pour cette destination.';
          }
        },
        error: (err) => {
          this.error = 'Erreur lors de la récupération des hôtels.';
          console.error('Search error:', err);
        }
      });
  }

  viewHotelDetails(hotel: Hotel) {
    this.loadingDetails = true;
    this.selectedHotel = hotel; // Show basic info immediately
    this.bookingForm.hotelId = hotel.id;

    this.hotelService.getHotelDetails(hotel.id)
      .pipe(finalize(() => this.loadingDetails = false))
      .subscribe({
        next: (detailedHotel) => {
          this.selectedHotel = detailedHotel;
        },
        error: (err) => {
          console.error('Error fetching hotel details:', err);
          // We already have basic hotel info from the search results
        }
      });
  }

  bookHotel() {
    if (!this.selectedHotel) return;

    this.bookingInProgress = true;
    this.bookingConfirmation = null;

    this.hotelService.bookHotel(this.bookingForm)
      .pipe(finalize(() => this.bookingInProgress = false))
      .subscribe({
        next: (confirmation) => {
          this.bookingConfirmation = confirmation;
        },
        error: (err) => {
          this.error = 'Erreur lors de la réservation de l\'hôtel.';
          console.error('Booking error:', err);
        }
      });
  }

  backToResults() {
    this.selectedHotel = null;
    this.bookingConfirmation = null;
  }

  // Helper methods
  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  private addDays(date: Date, days: number): Date {
    const result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
  }

  // Calculate total price based on current booking form
  calculateTotalPrice(): number {
    if (!this.selectedHotel || !this.selectedHotel.price) return 0;

    const checkIn = new Date(this.bookingForm.checkIn);
    const checkOut = new Date(this.bookingForm.checkOut);
    const nights = Math.max(1, Math.ceil((checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24)));

    return this.selectedHotel.price * this.bookingForm.rooms * nights;
  }
}
