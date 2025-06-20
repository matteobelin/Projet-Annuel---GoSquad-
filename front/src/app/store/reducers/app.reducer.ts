import {createReducer, on} from '@ngrx/store';
import {initialState} from '../app.state';
import * as AppActions from '../actions/app.actions';

/**
 * AppState interface defines the structure of the application state
 */
export const appReducer = createReducer(
  initialState,
  on(AppActions.setAppName, (state, { appName }) => ({
    ...state,
    appName
  })),
);
