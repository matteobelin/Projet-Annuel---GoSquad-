import { Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faEnvelope, faCalendarAlt, faMagnifyingGlass, faTableColumns, faBell } from '@fortawesome/free-solid-svg-icons';

// Ajoutez les icônes spécifiques à la bibliothèque
library.add(faEnvelope, faCalendarAlt, faMagnifyingGlass, faTableColumns, faBell);

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [FontAwesomeModule], // Assurez-vous que FontAwesomeModule est bien importé
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent { }
