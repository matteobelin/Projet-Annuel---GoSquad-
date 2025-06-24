import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {setAdvisorById} from './advisor.actions';
import * as AdvisorSelectors from './advisor.selectors';
import {AppState} from '../app.state';
import {selectCurrentAdvisor} from './advisor.selectors';
import {Advisor} from '../../core/models/advisor.model';

@Injectable({
  providedIn: 'root'
})
export class AdvisorStoreService {
  constructor(private store: Store<AppState>) {}

  // Selectors
  getCurrentAdvisor(): Observable<Advisor | undefined> {
    return this.store.select(selectCurrentAdvisor);
  }
}
