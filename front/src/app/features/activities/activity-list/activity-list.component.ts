import { Component, Signal, computed, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {NgIf, NgFor} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import {CustomerActivityService,CustomerActivityResponse, TravelActivity} from "../../../core/services/activityCustomer.service"

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

  private readonly customerActivityService = inject(CustomerActivityService)
  customerActivities = signal<CustomerActivityResponse[]>([]);
  selectedDate = signal<string | null>(null);
  errorMessage = signal<string | null>(null);

  private readonly activityStore = inject(ActivityStore);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  filterText = signal('');
  private removedActivityIds = signal<Set<string>>(new Set());

  private activities: Signal<Activity[]>;

  OnSelectedActivity:boolean
  currentActivityId: number | null = null;
  isTravelActivityMode = false;
  voyageId: string | null = null;
  startDate: string | null = null;
  groupeId:string|null = null;
  endDate: string | null = null;
  destination: string | null = null;
  constructor() {
    this.activityStore.loadActivities();
    this.activities = toSignal(this.activityStore.getActivities(), { initialValue: [] });
    this.OnSelectedActivity = false

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isTravelActivityMode = true;
        this.voyageId = params['id'];
        this.groupeId = params["groupId"];
        this.startDate = params['startDate'];
        this.endDate = params['endDate'];
        this.destination = params['destination'];
      }
    });
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

  private formatDateTimeLocal(dateStr: string | null): string | null {
    if (!dateStr) return null;
    const date = new Date(dateStr);
    const pad = (n: number) => n.toString().padStart(2, '0');
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
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

  goBack(){
    this.router.navigate(["/voyages/",this.voyageId])
  }


  AddActivityCustomer(activityId: number | undefined) {
    if (!activityId) {
      this.errorMessage.set('ID d’activité invalide');
      return;
    }
    if (!this.groupeId) {
      this.errorMessage.set('Group ID manquant');
      return;
    }
    this.OnSelectedActivity = true
    this.currentActivityId = activityId; // stocker l’id de l’activité choisie
    this.customerActivityService.getAllCustomerActivities(Number(this.groupeId)).subscribe({
      next: activities => {
        this.customerActivities.set(activities);
        this.errorMessage.set(null);
      },
      error: err => {
        this.errorMessage.set('Erreur lors de la récupération des activités clients : ' + err.message);
      }
    });
  }

  onDateChange(event: Event) {
    const input = event.target as HTMLInputElement | null;
    if (!input) return;
    const date = input.value;
    this.selectedDate.set(date);
    this.errorMessage.set(null);

    const conflict = this.customerActivities().some(act =>
      this.isDateBetween(date, act.startDate, act.endDate)
    );

    if (conflict) {
      this.errorMessage.set('Cette date est déjà prise par une activité existante.');
      this.selectedDate.set(null);
    }
  }

  isDateBetween(date: string, start: string, end: string): boolean {
    const d = new Date(date).getTime();
    const s = new Date(start).getTime();
    const e = new Date(end).getTime();
    return d >= s && d <= e;
  }

  confirmDate() {
    if (!this.selectedDate()) {
      this.errorMessage.set('Veuillez choisir une date valide.');
      return;
    }

    const startDateStr = this.selectedDate()!;
    const startDateObj = new Date(startDateStr);

    // Ajouter 3 heures
    const endDateObj = new Date(startDateObj.getTime() + 3 * 60 * 60 * 1000);

    const travelActivity: TravelActivity = {
      travelId: this.voyageId!,
      activityId: this.currentActivityId!,
      startDate: startDateObj.toISOString(),
      endDate: endDateObj.toISOString(),
      groupId: Number(this.groupeId),
    };

    this.customerActivityService.createTravelActivity(travelActivity).subscribe({
      next: () => {
        console.log('TravelActivity créée avec succès !');
        this.errorMessage.set(null);
        this.OnSelectedActivity = false
      },
      error: err => {
        this.errorMessage.set('Erreur lors de la création de l’activité : ' + err.message);
      }
    });
  }

}
