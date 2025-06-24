/**
 * AppState interface defines the structure of the application state
 */
export interface AppState {
  appName: string;
}

/**
 * initialState defines the initial state of the application
 */
export const initialState: AppState = {
  appName: 'GoSquad',
};
