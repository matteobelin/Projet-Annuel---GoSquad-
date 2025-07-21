import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class FlightConverter {
  // converter to convert raw flight data to the Flight model
  convert(rawFlights: any[]): any[] {
    return (rawFlights || []).map(f => {
      // Generate random data for the flight
      const randomPrice = (Math.random() * 1500 + 50).toFixed(2);
      const randomDuration = Math.floor(Math.random() * 8 + 1) * 60;
      const randomCO2 = (Math.random() * 200 + 50).toFixed(1);
      const baggage = Math.random() > 0.5 ? '1 bagage cabine, 1 bagage soute' : '1 bagage cabine';
      const departureDate = new Date(f.flightDate);
      const arrivalDate = new Date(departureDate.getTime() + randomDuration * 60000);

      return {
        departureCity: f.departureIata,
        arrivalCity: f.arrivalIata,
        departureAirport: f.departureIata,
        arrivalAirport: f.arrivalIata,
        departureDateTime: departureDate.toISOString(),
        arrivalDateTime: arrivalDate.toISOString(),
        airline: f.airline,
        bookingLink: undefined,
        flightNumber: f.flightNumber,
        price: randomPrice,
        duration: randomDuration,
        co2: randomCO2,
        baggage,
      };
    });
  }
}

