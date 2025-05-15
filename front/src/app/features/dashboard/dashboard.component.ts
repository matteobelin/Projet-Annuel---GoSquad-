import { Component } from '@angular/core';
import {MainHeaderComponent} from '../presenter/main-header/main-header.component';
import {AdvisorService} from '../../module/advisor/advisor.service';
import {AdvisorModel} from '../../model/advisor.model';

@Component({
  selector: 'app-dashboard',
  standalone:true,
  imports: [
    MainHeaderComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  constructor(private advisorService:AdvisorService){
    this.advisorService.getAllAdvisors().subscribe(
      (response:AdvisorModel[])=>{
        this.advisors=response
      }
    )
  }
  advisors : AdvisorModel[] = [];

}
