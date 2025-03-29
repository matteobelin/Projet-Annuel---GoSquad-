import { Component } from '@angular/core';
import {MainHeaderComponent} from "../presenter/main-header/main-header.component";

@Component({
  selector: 'app-accommodations',
    imports: [
        MainHeaderComponent
    ],
  templateUrl: './accommodations.component.html',
  styleUrl: './accommodations.component.css'
})
export class AccommodationsComponent {

}
