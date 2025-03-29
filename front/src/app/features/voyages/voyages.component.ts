import { Component } from '@angular/core';
import {MainHeaderComponent} from "../presenter/main-header/main-header.component";

@Component({
  selector: 'app-voyages',
    imports: [
        MainHeaderComponent
    ],
  templateUrl: './voyages.component.html',
  styleUrl: './voyages.component.css'
})
export class VoyagesComponent {

}
