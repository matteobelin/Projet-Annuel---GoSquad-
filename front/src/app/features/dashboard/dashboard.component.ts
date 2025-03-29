import { Component } from '@angular/core';
import {MainHeaderComponent} from '../presenter/main-header/main-header.component';

@Component({
  selector: 'app-dashboard',
  imports: [
    MainHeaderComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {

}
