import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { map, mergeMap, catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

import * as ActivityActions from './activity.actions';
import { environment } from '../../../environments/environment';
import { Activity } from '../../core/models/activity.model';

@Injectable()
export class ActivityEffects {
  private readonly actions$ = inject(Actions);
  private readonly http = inject(HttpClient);

  // Endpoints (adaptés à ton backend)
  private readonly apiUrl = environment.apiUrl;
  private readonly getAllActivitiesUrl = `${this.apiUrl}/activity/all`;
  private readonly getActivitiesByCategoryUrl = `${this.apiUrl}/activity/by-category`;
  private readonly getActivityByIdUrl = `${this.apiUrl}/activity/by-id`; // ?id=
  private readonly createActivityUrl = `${this.apiUrl}/activity`;
  private readonly updateActivityUrl = `${this.apiUrl}/activity`;
  private readonly deleteActivityUrl = `${this.apiUrl}/activity`;

  loadActivities$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ActivityActions.loadActivities),
      mergeMap(() =>
        this.http.get<Activity[]>(this.getAllActivitiesUrl).pipe(
          map(activities => ActivityActions.loadActivitiesSuccess({ activities })),
          catchError(error => of(ActivityActions.loadActivitiesFailure({ error })))
        )
      )
    )
  );

  loadActivitiesByCategory$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ActivityActions.loadActivitiesByCategory),
      mergeMap(() =>
        this.http.get<Activity[]>(this.getActivitiesByCategoryUrl).pipe(
          map(activities => ActivityActions.loadActivitiesByCategorySuccess({ activities })),
          catchError(error => of(ActivityActions.loadActivitiesByCategoryFailure({ error })))
        )
      )
    )
  );

  loadActivityById$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ActivityActions.loadActivityById),
      mergeMap(action =>
        this.http.get<Activity>(`${this.getActivityByIdUrl}?id=${action.id}`).pipe(
          map(activity => ActivityActions.loadActivityByIdSuccess({ activity })),
          catchError(error => of(ActivityActions.loadActivityByIdFailure({ error })))
        )
      )
    )
  );

  createActivity$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ActivityActions.createActivity),
      mergeMap(action =>
        this.http.post<Activity>(this.createActivityUrl, action.activity).pipe(
          map(() => ActivityActions.createActivitySuccess({ message: 'Activity created successfully.' })),
          catchError(error => of(ActivityActions.createActivityFailure({ error })))
        )
      )
    )
  );

  updateActivity$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ActivityActions.updateActivity),
      mergeMap(action =>
        this.http.put<Activity>(this.updateActivityUrl, action.activity).pipe(
          map(() => ActivityActions.updateActivitySuccess({ message: 'Activity updated successfully.' })),
          catchError(error => of(ActivityActions.updateActivityFailure({ error })))
        )
      )
    )
  );

  deleteActivity$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ActivityActions.deleteActivity),
      mergeMap(action => {
        const options = {
          body: action.activity // `ActivityRequestDTO` attendu par backend
        };
        return this.http.delete<string>(this.deleteActivityUrl, options).pipe(
          map(() => ActivityActions.deleteActivitySuccess({ message: 'Activity deleted successfully.' })),
          catchError(error => of(ActivityActions.deleteActivityFailure({ error })))
        );
      })
    )
  );
}
