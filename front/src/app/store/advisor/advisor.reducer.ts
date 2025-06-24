import { createReducer, on } from '@ngrx/store';
import { Advisor } from '../../core/models/advisor.model';
import * as AdvisorActions from './advisor.actions';
import {setAdvisorByIdSuccess} from './advisor.actions';

export interface AdvisorState {
  currentAdvisor?: Advisor;
  loading: boolean;
  error: any;
}

export const initialState: AdvisorState = {
  //currentAdvisor: undefined,
  loading: false,
  error: null
};

export const advisorReducer = createReducer(
  initialState,
  on(AdvisorActions.setAdvisorById, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(AdvisorActions.setAdvisorByIdSuccess, (state, { advisor }) => ({
    ...state,
    currentAdvisor: advisor,
    loading: false,
    error: null
    }
  )),
  on(AdvisorActions.setAdvisorByIdFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error: error
  }))
);
