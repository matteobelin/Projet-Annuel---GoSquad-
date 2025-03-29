import {Component} from '@angular/core';
import {MenuModule} from 'primeng/menu';
import {RouterLink, RouterLinkActive} from '@angular/router';


@Component({
  selector: 'app-feature-nav',
  imports: [
    MenuModule,
    RouterLink,
    RouterLinkActive,
  ],
  templateUrl: './feature-nav.component.html',
  standalone: true,
  styleUrl: './feature-nav.component.css'
})
export class FeatureNavComponent{
  /** Menu item for feature navigation. */
  featureItems = [
    { label: 'Dashboard', icon: 'fas fa-th-large', routerLink: ['/dashboard'] },
    { label: 'Clients', icon: 'fas fa-users', routerLink: ['/clients'] },
    { label: 'Voyages', icon: 'fas fa-map', routerLink: ['/voyages'] },
    { label: 'Activités', icon: 'fas fa-hiking', routerLink: ['/activities'] },
    { label: 'Hébergements', icon: 'fas fa-bed', routerLink: ['/accommodations'] },
    { label: 'Transports', icon: 'fas fa-car', routerLink: ['/transports'] },
    { label: 'Paiements', icon: 'fas fa-credit-card', routerLink: ['/payments'] },
    { label: 'Documents', icon: 'fas fa-file', routerLink: ['/documents'] },
    { label: 'Notifications', icon: 'fas fa-bell', routerLink: ['/notifications'] },
    { label: 'Historique', icon: 'fas fa-clock', routerLink: ['/history'] },
    { label: 'Statistiques', icon: 'fas fa-chart-bar', routerLink: ['/statistics'] },
    { label: 'Administration', icon: 'fas fa-cogs', routerLink: ['/administration'] },
    { label: 'Support', icon: 'fas fa-question-circle', routerLink: ['/support'] }
  ];
}
