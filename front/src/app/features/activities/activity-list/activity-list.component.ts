import { Component, Signal, computed, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {NgIf, NgFor} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { Activity } from '../../../core/models/activity.model';
import { ActivityStore } from '../../../store/activities/activity.store.service';

@Component({
  selector: 'app-activity-list',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule],
  templateUrl: './activity-list.component.html',
  styleUrls: ['./activity-list.component.css']
})
export class ActivityListComponent {

  private readonly activityStore = inject(ActivityStore);
  private readonly router = inject(Router);

  filterText = signal('');
  private removedActivityIds = signal<Set<string>>(new Set());

  private activities: Signal<Activity[]>;

  constructor() {
    this.activityStore.loadActivities();
    this.activities = toSignal(this.activityStore.getActivities(), { initialValue: [] });
  }

  /**
   * Liste filtrée des activités selon le champ de recherche.
   */
  filteredActivities = computed(() => {
    const filter = this.filterText().toLowerCase();
    const removedIds = this.removedActivityIds();

    return this.activities().filter(activity =>
      (
        (activity.name ?? '').toLowerCase().includes(filter) ||
        (activity.city ?? '').toLowerCase().includes(filter) ||
        (activity.country ?? '').toLowerCase().includes(filter) ||
        (activity.categoryName ?? '').toLowerCase().includes(filter)
      )
    );
  });

  /**
   * Même liste que filteredActivities, pour éviter bug d’appel dans *ngIf et *ngFor.
   */
  filteredActivitiess(): Activity[] {
    return this.filteredActivities();
  }

  onEdit(id: number | undefined): void {
    if (id === undefined) {
      console.error('ID est undefined');
      return;
    }
    this.router.navigate(['/activities/edit', id]);
  }

  onAdd(){
    this.router.navigate(["/activities/add"])
  }


}
