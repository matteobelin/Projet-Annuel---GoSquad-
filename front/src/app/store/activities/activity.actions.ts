import { createAction, props } from '@ngrx/store';
import { Activity } from '../../core/models/activity.model';

// Load All Activities
export const loadActivities = createAction('[Activity] Load All Activities');
export const loadActivitiesSuccess = createAction(
  '[Activity] Load All Activities Success',
  props<{ activities: Activity[] }>()
);
export const loadActivitiesFailure = createAction(
  '[Activity] Load All Activities Failure',
  props<{ error: any }>()
);

// Load Activities By Category
export const loadActivitiesByCategory = createAction('[Activity] Load Activities By Category');
export const loadActivitiesByCategorySuccess = createAction(
  '[Activity] Load Activities By Category Success',
  props<{ activities: Activity[] }>()
);
export const loadActivitiesByCategoryFailure = createAction(
  '[Activity] Load Activities By Category Failure',
  props<{ error: any }>()
);

// Load Activity By ID
export const loadActivityById = createAction(
  '[Activity] Load Activity By ID',
  props<{ id: number }>()
);
export const loadActivityByIdSuccess = createAction(
  '[Activity] Load Activity By ID Success',
  props<{ activity: Activity }>()
);
export const loadActivityByIdFailure = createAction(
  '[Activity] Load Activity By ID Failure',
  props<{ error: any }>()
);

// Create Activity
export const createActivity = createAction(
  '[Activity] Create Activity',
  props<{ activity: Activity }>()
);
export const createActivitySuccess = createAction(
  '[Activity] Create Activity Success',
  props<{ message: string }>()
);
export const createActivityFailure = createAction(
  '[Activity] Create Activity Failure',
  props<{ error: any }>()
);

// Update Activity
export const updateActivity = createAction(
  '[Activity] Update Activity',
  props<{ activity: Activity }>()
);
export const updateActivitySuccess = createAction(
  '[Activity] Update Activity Success',
  props<{ message: string }>()
);
export const updateActivityFailure = createAction(
  '[Activity] Update Activity Failure',
  props<{ error: any }>()
);

// Delete Activity
export const deleteActivity = createAction(
  '[Activity] Delete Activity',
  props<{ activity: Activity }>()
);
export const deleteActivitySuccess = createAction(
  '[Activity] Delete Activity Success',
  props<{ message: string }>()
);
export const deleteActivityFailure = createAction(
  '[Activity] Delete Activity Failure',
  props<{ error: any }>()
);


