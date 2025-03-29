import { Component } from '@angular/core';
import {MainHeaderComponent} from "../presenter/main-header/main-header.component";

@Component({
  selector: 'app-history',
    imports: [
        MainHeaderComponent
    ],
  templateUrl: './history.component.html',
  styleUrl: './history.component.css'
})
export class HistoryComponent {

}
