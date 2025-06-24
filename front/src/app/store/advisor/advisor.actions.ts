import { createAction, props } from '@ngrx/store';
import { Advisor } from '../../core/models/advisor.model';

export const setAdvisorById = createAction(
  '[Advisor] Set Advisor By Id',
  props<{ id: number }>()
);

export const setAdvisorByIdSuccess = createAction(
  '[Advisor] Set Advisor By Id Success',
  props<{ advisor: Advisor }>()
);

export const setAdvisorByIdFailure = createAction(
  '[Advisor] Set Advisor By Id Failure',
  props<{ error: any }>()
);

export const setCurrentAdvisor = createAction(
  '[Advisor] Set Current Advisor',
  props<{ advisor: Advisor }>()
);
