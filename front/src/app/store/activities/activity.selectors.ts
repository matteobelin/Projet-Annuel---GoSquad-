import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ActivityReducer } from './activity.reducer';
import { Activity } from '../../core/models/activity.model';

// Sélecteur racine de la feature "activity"
export const selectActivityState = createFeatureSelector<ActivityReducer>('activity');

/**
 * Sélecteur : liste de toutes les activités.
 */
export const selectActivities = createSelector(
  selectActivityState,
  (state: ActivityReducer) => state.activities
);

/**
 * Sélecteur : état de chargement des activités.
 */
export const selectActivitiesLoading = createSelector(
  selectActivityState,
  (state: ActivityReducer) => state.loading
);

/**
 * Sélecteur : activité actuellement sélectionnée.
 */
export const selectSelectedActivity = createSelector(
  selectActivityState,
  (state: ActivityReducer): Activity | null => state.selectedActivity ?? null
);

/**
 * Sélecteur : erreurs éventuelles côté activité.
 */
export const selectActivityError = createSelector(
  selectActivityState,
  (state: ActivityReducer) => state.error
);
