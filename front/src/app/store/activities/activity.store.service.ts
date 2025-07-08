import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Activity } from '../../core/models/activity.model';

import * as ActivityActions from './activity.actions';
import * as ActivitySelectors from './activity.selectors';
import { ActivityReducer } from './activity.reducer'; // Remplacer par ActivityState si tu l’as défini ainsi

@Injectable({
  providedIn: 'root'
})
export class ActivityStore {
  constructor(private store: Store<ActivityReducer>) {}

  // Dispatcher : charger toutes les activités
  loadActivities(): void {
    this.store.dispatch(ActivityActions.loadActivities());
  }

  // Selector : observable de la liste des activités
  getActivities(): Observable<Activity[]> {
    return this.store.select(ActivitySelectors.selectActivities);
  }

  // Dispatcher : charger par catégorie
  loadActivitiesByCategory(): void {
    this.store.dispatch(ActivityActions.loadActivitiesByCategory());
  }

  // Dispatcher : charger une activité par ID
  loadActivityById(id: number): void {
    this.store.dispatch(ActivityActions.loadActivityById({id: id}));
  }

  // Selector : activité actuellement sélectionnée
  getSelectedActivity(): Observable<Activity | null> {
    return this.store.select(ActivitySelectors.selectSelectedActivity);
  }

  // Dispatcher : création d’une nouvelle activité
  createActivity(activity: Activity): void {
    this.store.dispatch(ActivityActions.createActivity({ activity }));
  }

  // Dispatcher : mise à jour d’une activité existante
  updateActivity(activity: Activity): void {
    this.store.dispatch(ActivityActions.updateActivity({ activity }));
  }

  // Dispatcher : suppression d’une activité
  deleteActivity(activity: Activity): void {
    this.store.dispatch(ActivityActions.deleteActivity({ activity }));
  }

  // Optionnel : observables pour loading & error
  getLoading(): Observable<boolean> {
    return this.store.select(ActivitySelectors.selectActivitiesLoading);
  }

  getError(): Observable<any> {
    return this.store.select(ActivitySelectors.selectActivityError);
  }
}
