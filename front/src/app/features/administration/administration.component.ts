import { Component } from '@angular/core';
import {MainHeaderComponent} from "../presenter/main-header/main-header.component";

@Component({
  selector: 'app-administration',
    imports: [
        MainHeaderComponent
    ],
  templateUrl: './administration.component.html',
  styleUrl: './administration.component.css'
})
export class AdministrationComponent {

}
