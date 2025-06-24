import { createReducer, on } from '@ngrx/store';
import * as FlightActions from './flight.actions';
import { Flight } from '../../core/models/flight.model';

export interface FlightState {
  searchResults: Flight[];
  selectedFlight: Flight | null;
  loading: boolean;
  error: string | null;
}

export const initialState: FlightState = {
  searchResults: [],
  selectedFlight: null,
  loading: false,
  error: null
};

export const flightReducer = createReducer(
  initialState,
  on(FlightActions.searchFlights, state => ({ ...state, loading: true, error: null })),
  on(FlightActions.searchFlightsSuccess, (state, { results }) => ({
    ...state, searchResults: results, loading: false
  })),
  on(FlightActions.searchFlightsFailure, (state, { error }) => ({
    ...state, error, loading: false
  })),
  on(FlightActions.selectFlight, (state, { flight }) => ({
    ...state, selectedFlight: flight
  }))
);
