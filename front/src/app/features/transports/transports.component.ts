import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FlightService } from '../../core/services/flight.service';
import { Flight } from '../../core/models/flight.model';
import { MainHeaderComponent } from '../presenter/main-header/main-header.component';
import { FlightConverter } from './flight.converter';

@Component({
  selector: 'app-transports',
  standalone: true,
  imports: [MainHeaderComponent, CommonModule, FormsModule],
  templateUrl: './transports.component.html',
  styleUrl: './transports.component.css',
})
export class TransportsComponent {
  /* departure from */
  from = '';
  /* arrival */
  to = '';
  /* date of flight */
  date = '';
  /* results */
  results: Flight[] = [];
  /* loading data state */
  loading = false;
  /* error */
  error: string | null = null;
  /* MaxPrice */
  maxPrice?: number;
  /* Max duration */
  maxDuration?: number;
  /* Max Co2 */
  maxCO2?: number;
  /* Cities */
  cities = [
    { name: 'New York - John F. Kennedy', code: 'JFK' },
    { name: 'Los Angeles - LAX', code: 'LAX' },
    { name: 'London - Heathrow', code: 'LHR' },
    { name: 'Paris - Charles de Gaulle', code: 'CDG' },
    { name: 'Tokyo - Haneda', code: 'HND' },
    { name: 'Beijing - Capital', code: 'PEK' },
    { name: 'Dubai - Dubai Intl', code: 'DXB' },
    { name: 'Singapore - Changi', code: 'SIN' },
    { name: 'Shanghai - Pudong', code: 'PVG' },
    { name: 'Istanbul - Istanbul Airport', code: 'IST' },
    { name: 'São Paulo - Guarulhos', code: 'GRU' },
    { name: 'Mexico City - Benito Juarez', code: 'MEX' },
    { name: 'Moscow - Sheremetyevo', code: 'SVO' },
    { name: 'Mumbai - Chhatrapati Shivaji', code: 'BOM' },
    { name: 'Delhi - Indira Gandhi', code: 'DEL' },
    { name: 'Jakarta - Soekarno-Hatta', code: 'CGK' },
    { name: 'Seoul - Incheon', code: 'ICN' },
    { name: 'Bangkok - Suvarnabhumi', code: 'BKK' },
    { name: 'Toronto - Pearson', code: 'YYZ' },
    { name: 'Sydney - Kingsford Smith', code: 'SYD' }
  ];

  /**
   * Constructor
   */
  constructor(private flightService: FlightService, private flightConverter: FlightConverter) {}

  /**
   * Method to search for flights based on user input
   */
  onSearch() {
    this.loading = true;
    this.error = null;
    this.results = [];

    this.flightService.searchFlights(this.from, this.to).subscribe({
      next: (response) => {
        console.log('Response :', response);
        // Utilisation du converter pour enrichir les données
        const enriched = this.flightConverter.convert(response || []);
        // Ajout du nom des villes à partir des codes IATA
        let filtered = enriched.map((f: EnrichedFlight) => ({
          ...f,
          departureCity: this.getCityName(f.departureAirport),
          arrivalCity: this.getCityName(f.arrivalAirport)
        }));
        // Application des filtres
        if (this.maxPrice) {
          filtered = filtered.filter(f => parseFloat(f.price) <= this.maxPrice!);
        }
        if (this.maxDuration) {
          filtered = filtered.filter(f => f.duration <= this.maxDuration!);
        }
        if (this.maxCO2) {
          filtered = filtered.filter(f => parseFloat(f.co2) <= this.maxCO2!);
        }
        this.results = filtered;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message || 'Erreur lors de la recherche de vols';
        this.loading = false;
      }
    });
  }

  /**
   * Retourne le nom de la ville à partir du code IATA
   * @param iataCode
   */
  getCityName(iataCode: string): string {
    const match = this.cities.find(c => c.code === iataCode);
    return match ? match.name : iataCode;
  }

  /**
   * On select flight
   * @param flight
   */
  onSelectFlight(flight: Flight) {
    // Assigner le vol sélectionné à un client
    console.log('Vol sélectionné :', flight);
  }
}
