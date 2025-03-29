import { Component } from '@angular/core';
import {MainHeaderComponent} from "../presenter/main-header/main-header.component";

@Component({
  selector: 'app-statistics',
    imports: [
        MainHeaderComponent
    ],
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.css'
})
export class StatisticsComponent {

}
