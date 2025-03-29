import { Component } from '@angular/core';
import {MainHeaderComponent} from "../presenter/main-header/main-header.component";

@Component({
  selector: 'app-activities',
    imports: [
        MainHeaderComponent
    ],
  templateUrl: './activities.component.html',
  styleUrl: './activities.component.css'
})
export class ActivitiesComponent {

}
