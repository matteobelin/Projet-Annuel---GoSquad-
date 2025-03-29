import { Component } from '@angular/core';
import {MainHeaderComponent} from "../presenter/main-header/main-header.component";

@Component({
  selector: 'app-clients',
    imports: [
        MainHeaderComponent
    ],
  templateUrl: './clients.component.html',
  styleUrl: './clients.component.css'
})
export class ClientsComponent {

}
