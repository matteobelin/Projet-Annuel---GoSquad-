import {createReducer, on} from '@ngrx/store';
import {initialAppState} from '../app-state';
import {GosquadMainActions} from '../actions/gosquad-main.action';

export const GosquadMainReducer = createReducer(
  initialAppState,
  on(GosquadMainActions.reloadApp, state => ({
    ...state,
    reloadApp: true
  }))
);
