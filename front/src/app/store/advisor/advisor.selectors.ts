import { createFeatureSelector, createSelector } from '@ngrx/store';
import { AdvisorState } from './advisor.reducer';
import {Advisor} from '../../core/models/advisor.model';

export const selectAdvisorState = createFeatureSelector<AdvisorState>('advisor');

export const selectCurrentAdvisor = createSelector(
  selectAdvisorState,
  (state: AdvisorState): Advisor | undefined => {
    return state.currentAdvisor;
  }
);

export const selectLoading = createSelector(
  selectAdvisorState,
  (state: AdvisorState) => state.loading
);

export const selectError = createSelector(
  selectAdvisorState,
  (state: AdvisorState) => state.error
);
