import { Routes } from '@angular/router';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { CustomerListComponent } from './features/clients/customer-list/customer-list.component';
import { VoyagesComponent } from './features/voyages/voyages.component';
import { ActivitiesComponent } from './features/activities/activities.component';
import { AccommodationsComponent } from './features/accommodations/accommodations.component';
import { TransportsComponent } from './features/transports/transports.component';
import { PaymentsComponent } from './features/payments/payments.component';
import { DocumentsComponent } from './features/documents/documents.component';
import { NotificationsComponent } from './features/notifications/notifications.component';
import { HistoryComponent } from './features/history/history.component';
import { StatisticsComponent } from './features/statistics/statistics.component';
import { AdministrationComponent } from './features/administration/administration.component';
import { SupportComponent } from './features/support/support.component';
import { LoginComponent } from './shared/auth/login/login.component';
import { authGuard } from './shared/auth/guards/auth.guard';
import { AdvisorComponent } from './module/advisor/advisor.component';
import { CustomerDetailComponent } from './features/clients/customer-detail/customer-detail.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: AdvisorComponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'clients', component: CustomerListComponent },
      { path: 'clients/:id', component: CustomerDetailComponent },
      { path: 'voyages', component: VoyagesComponent },
      { path: 'activities', component: ActivitiesComponent },
      { path: 'accommodations', component: AccommodationsComponent },
      { path: 'transports', component: TransportsComponent },
      { path: 'payments', component: PaymentsComponent },
      { path: 'documents', component: DocumentsComponent },
      { path: 'notifications', component: NotificationsComponent },
      { path: 'history', component: HistoryComponent },
      { path: 'statistics', component: StatisticsComponent },
      { path: 'administration', component: AdministrationComponent },
      { path: 'support', component: SupportComponent },
    ]
  },
  { path: '**', redirectTo: '' }
];
