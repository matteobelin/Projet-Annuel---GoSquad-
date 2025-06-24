/**
 * Effects triggered on actions related to the main Gosquad functionality.
 */
import {inject, Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {EMPTY, switchMap} from 'rxjs';
import {GosquadMainActions} from '../actions/gosquad-main.action';

@Injectable()
export class GosquadMainEffects {
  // ============================================================ //
  // ========================== FIELDS ========================== //
  // ============================================================ //
  /** Actions stream.*/
  constructor(private readonly actions$: Actions) {}

  // ============================================================ //
  // ======================= RELOAD APP ========================= //
  // ============================================================ //
  /**
   * Reload apps.
   */
  reloadApp = createEffect(() => {
    return this.actions$.pipe(
      ofType(GosquadMainActions.reloadAppData),
      switchMap(() => {

        return EMPTY;
      })
    );
  }, {dispatch: false});
}
