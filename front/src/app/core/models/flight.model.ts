export interface Flight {
  departureCity: string;
  arrivalCity: string;
  departureAirport: string;
  arrivalAirport: string;
  departureDateTime: string; // ISO string
  arrivalDateTime: string;   // ISO string
  airline: string;
  bookingLink?: string;      // facultatif
  flightNumber: string;
}
