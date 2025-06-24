import {inject, Injectable} from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { map, mergeMap, catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import * as AdvisorActions from './advisor.actions';
import { environment } from '../../../environments/environment';
import { Advisor } from '../../core/models/advisor.model';

@Injectable()
export class AdvisorEffects {
  private readonly actions$ = inject(Actions);

  private apiUrl = `${environment.apiUrl}/getAdvisor`;

  /**
   * Effect to load the current advisor by ID.
   */
  loadCurrentAdvisor$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AdvisorActions.setAdvisorById),
      mergeMap(advisorTokenInfo =>
        this.http.get<Advisor>(`${this.apiUrl}?id=${advisorTokenInfo.id}`).pipe(
          map(advisor => AdvisorActions.setAdvisorByIdSuccess({ advisor })),
          catchError(error => of(AdvisorActions.setAdvisorByIdFailure({ error })))
        )
      )
    )
  );

  constructor(
    private http: HttpClient
  ) {}
}
