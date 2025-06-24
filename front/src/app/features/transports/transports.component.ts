import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FlightService } from '../../core/services/flight.service';
import { Flight } from '../../core/models/flight.model';
import { MainHeaderComponent } from '../presenter/main-header/main-header.component';

@Component({
  selector: 'app-transports',
  standalone: true,
  imports: [MainHeaderComponent, CommonModule, FormsModule],
  templateUrl: './transports.component.html',
  styleUrl: './transports.component.css'
})
export class TransportsComponent {
  from = '';
  to = '';
  date = '';
  results: Flight[] = [];
  loading = false;
  error: string | null = null;

  constructor(private flightService: FlightService) {}

  onSearch() {
    this.loading = true;
    this.error = null;
    this.results = [];
    this.flightService.searchFlights(this.from, this.to).subscribe({
      next: (response) => {
        console.log("la reponse de l'API :", response);
        this.results = (response || []).map((f: any) => ({
          departureCity: f.departureIata,
          arrivalCity: f.arrivalIata,
          departureAirport: f.departureIata,
          arrivalAirport: f.arrivalIata,
          departureDateTime: f.flightDate,
          arrivalDateTime: f.flightDate,
          airline: f.airline,
          bookingLink: undefined,
          flightNumber: f.flightNumber
        }));
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message || 'Erreur lors de la recherche de vols';
        this.loading = false;
      }
    });
  }

  selectFlight(flight: Flight) {
    // Action locale ou navigation, selon besoin
    console.log('Vol sélectionné :', flight);
  }
}
