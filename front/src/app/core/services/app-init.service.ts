import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../store/app.state';
import { TokenService } from './token.service';
import {setAdvisorById, setCurrentAdvisor} from '../../store/advisor/advisor.actions';
import { Advisor } from '../models/advisor.model';
import {dateTimestampProvider} from 'rxjs/internal/scheduler/dateTimestampProvider';

@Injectable({
  providedIn: 'root'
})
export class AppInitService {
  constructor(
    private store: Store<AppState>,
    private tokenService: TokenService
  ) {}

  initializeApp(): Promise<void> {
    return new Promise((resolve) => {
      const advisorData = this.tokenService.getAdvisorFromToken();

      if (advisorData) {
        const advisor: Advisor = {
          id: advisorData.id,
          firstname: advisorData.firstName,
          lastname: advisorData.lastName,
          email: advisorData.email,
          phone: '',
          role: 'NOT_SET',
          companyCode: advisorData.companyCode,
        };

        this.store.dispatch(setAdvisorById({id:advisor.id}));
      }

      resolve();
    });
  }
}
