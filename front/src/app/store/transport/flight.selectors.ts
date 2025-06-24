import { createSelector } from '@ngrx/store';
import { FlightState } from './flight.reducer';

export const selectFlightState = (state: any) => state.flight;

export const selectFlightResults = createSelector(
  selectFlightState,
  (state: FlightState) => state.searchResults
);

export const selectFlightLoading = createSelector(
  selectFlightState,
  (state: FlightState) => state.loading
);

export const selectFlightError = createSelector(
  selectFlightState,
  (state: FlightState) => state.error
);
