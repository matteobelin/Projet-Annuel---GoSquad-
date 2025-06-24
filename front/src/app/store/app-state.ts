import { FlightState } from './transport/flight.reducer';

/**
 * Interface representing the state of the application.
 */
export interface AppState {
  flight: FlightState;
  // Ajoutez ici d'autres slices du state si besoin
}

/**
 * Initial value for the application state.
 */
export const initialAppState: AppState = {
  flight: {
    searchResults: [],
    selectedFlight: null,
    loading: false,
    error: null
  }
};
