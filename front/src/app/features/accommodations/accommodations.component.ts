import { Component } from '@angular/core';
import { MainHeaderComponent } from '../presenter/main-header/main-header.component';
import { FormsModule } from '@angular/forms';
import {HotelBookingComponent} from '../hotel-booking.component';

@Component({
  selector: 'app-accommodations',
  standalone: true,
  imports: [
    MainHeaderComponent,
    FormsModule,
    HotelBookingComponent
  ],
  templateUrl: './accommodations.component.html',
  styleUrl: './accommodations.component.css'
})
export class AccommodationsComponent {

}
