import { createAction, props } from '@ngrx/store';

export const setAppName = createAction(
  'Set App Name',
  props<{ appName: string }>()
);

