import {inject, Injectable} from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { HttpClient } from '@angular/common/http';
import { searchFlights, searchFlightsSuccess, searchFlightsFailure } from './flight.actions';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { Flight } from '../../core/models/flight.model';
import { environment } from '../../../environments/environment';
import * as FlightActions from './flight.actions';

@Injectable()
export class FlightEffects {
  private readonly actions$ = inject(Actions);

  constructor(private http: HttpClient) {
    console.log('actions$ dans FlightEffects :', this.actions$);
  }

  searchFlights$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(FlightActions.searchFlights),
      mergeMap(({ from, to }) =>
        this.http.get<any>(`http://localhost:8080/flights`, {
          params: {
            dep_iata: from,
            arr_iata: to
          }
        }).pipe(
          map(response => {
            const results: Flight[] = response.data.map((f: any) => ({
              departureCity: f.departure.city,
              arrivalCity: f.arrival.city,
              departureAirport: f.departure.airport,
              arrivalAirport: f.arrival.airport,
              departureDateTime: f.departure.scheduled,
              arrivalDateTime: f.arrival.scheduled,
              airline: f.airline.name,
              bookingLink: undefined,
              flightNumber: f.flight.number
            }));
            return searchFlightsSuccess({ results });
          }),
          catchError(error => of(searchFlightsFailure({ error: error.message })))
        )
      )
    );
  });
}
