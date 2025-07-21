import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainHeaderComponent } from '../presenter/main-header/main-header.component';
import { CustomerService, CustomerApiResponse } from '../../core/services/customer.service';
import { VoyageService, VoyageApiResponse } from '../../core/services/voyage.service';
import { FlightService } from '../../core/services/flight.service';
import { CustomerActivityService, CustomerActivityResponse } from '../../core/services/activityCustomer.service';
import { ActivityStore } from '../../store/activities/activity.store.service';
import { Activity } from '../../core/models/activity.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MainHeaderComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  customers: CustomerApiResponse[] = [];
  voyages: VoyageApiResponse[] = [];
  activities: Activity[] = [];
  flights: any[] = [];

  // Statistics
  totalCustomers: number = 0;
  totalVoyages: number = 0;
  totalActivities: number = 0;
  upcomingVoyages: number = 0;

  // Loading states
  loadingCustomers: boolean = true;
  loadingVoyages: boolean = true;
  loadingActivities: boolean = true;

  constructor(
    private customerService: CustomerService,
    private voyageService: VoyageService,
    private flightService: FlightService,
    private activityService: CustomerActivityService,
    private activityStore: ActivityStore
  ) {}

  ngOnInit(): void {
    this.loadCustomers();
    this.loadVoyages();
    // Activities require a groupId, so we'll load them after voyages if needed
  }

  loadCustomers(): void {
    this.loadingCustomers = true;
    this.customerService.getAllCustomers().subscribe({
      next: (data) => {
        this.customers = data;
        this.totalCustomers = data.length;
        this.loadingCustomers = false;
      },
      error: (error) => {
        console.error('Error loading customers:', error);
        this.loadingCustomers = false;
      }
    });
  }

  loadVoyages(): void {
    this.loadingVoyages = true;
    this.voyageService.getAllVoyages().subscribe({
      next: (data) => {
        this.voyages = data;
        this.totalVoyages = data.length;

        // Calculate upcoming voyages (those with a start date in the future)
        const today = new Date();
        this.upcomingVoyages = data.filter(voyage => {
          const startDate = new Date(voyage.startDate);
          return startDate > today;
        }).length;

        this.loadingVoyages = false;

        // Load activities from the store
        this.loadActivities();
      },
      error: (error) => {
        console.error('Error loading voyages:', error);
        this.loadingVoyages = false;
      }
    });
  }

  loadActivities(): void {
    this.loadingActivities = true;
    // Dispatch action to load activities
    this.activityStore.loadActivities();

    // Subscribe to the activities from the store
    this.activityStore.getActivities().subscribe({
      next: (data) => {
        this.activities = data;
        this.totalActivities = data.length;
        this.loadingActivities = false;
      },
      error: (error) => {
        console.error('Error loading activities:', error);
        this.loadingActivities = false;
      }
    });
  }
}
