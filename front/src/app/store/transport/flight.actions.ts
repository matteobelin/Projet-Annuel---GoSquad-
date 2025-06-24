import { createAction, props } from '@ngrx/store';
import { Flight } from '../../core/models/flight.model';

export const searchFlights = createAction(
  '[Flight] Search Flights',
  props<{ from: string; to: string }>()
);

export const searchFlightsSuccess = createAction(
  '[Flight] Search Flights Success',
  props<{ results: Flight[] }>()
);

export const searchFlightsFailure = createAction(
  '[Flight] Search Flights Failure',
  props<{ error: string }>()
);

export const selectFlight = createAction(
  '[Flight] Select Flight',
  props<{ flight: Flight }>()
);
