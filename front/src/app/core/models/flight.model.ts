export interface Flight {
  departureCity: string;
  arrivalCity: string;
  departureAirport: string;
  arrivalAirport: string;
  departureDateTime: string;
  arrivalDateTime: string;
  airline: string;
  bookingLink?: string;
  flightNumber: string;
  price: string;
  duration: number;
  co2: string;
  baggage: string;
}
