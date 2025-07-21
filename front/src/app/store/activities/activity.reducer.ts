import { createReducer, on } from '@ngrx/store';
import * as ActivityActions from './activity.actions';
import { Activity } from '../../core/models/activity.model';

export interface ActivityReducer {
  activities: Activity[];
  selectedActivity: Activity | null;
  loading: boolean;
  error: any;
  message: string | null;
}

export const initialState: ActivityReducer = {
  activities: [],
  selectedActivity: null,
  loading: false,
  error: null,
  message: null,
};

export const activityReducer = createReducer(
  initialState,

  // Load all
  on(ActivityActions.loadActivities, state => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(ActivityActions.loadActivitiesSuccess, (state, { activities }) => ({
    ...state,
    activities,
    loading: false,
  })),
  on(ActivityActions.loadActivitiesFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  })),

  // Load by category
  on(ActivityActions.loadActivitiesByCategory, state => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(ActivityActions.loadActivitiesByCategorySuccess, (state, { activities }) => ({
    ...state,
    activities,
    loading: false,
  })),
  on(ActivityActions.loadActivitiesByCategoryFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  })),

  // Load by ID
  on(ActivityActions.loadActivityById, state => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(ActivityActions.loadActivityByIdSuccess, (state, { activity }) => ({
    ...state,
    selectedActivity: activity,
    loading: false,
  })),
  on(ActivityActions.loadActivityByIdFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  })),

  // Create
  on(ActivityActions.createActivity, state => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(ActivityActions.createActivitySuccess, (state, { message }) => ({
    ...state,
    loading: false,
    message,
  })),
  on(ActivityActions.createActivityFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  })),

  // Update
  on(ActivityActions.updateActivity, state => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(ActivityActions.updateActivitySuccess, (state, { message }) => ({
    ...state,
    loading: false,
    message,
  })),
  on(ActivityActions.updateActivityFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  })),

  // Delete
  on(ActivityActions.deleteActivity, state => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(ActivityActions.deleteActivitySuccess, (state, { message }) => ({
    ...state,
    loading: false,
    message,
  })),
  on(ActivityActions.deleteActivityFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  }))

);
