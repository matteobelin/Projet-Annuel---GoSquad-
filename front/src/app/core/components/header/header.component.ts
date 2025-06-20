import {Component, Input, HostListener} from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faEnvelope, faCalendarAlt, faMagnifyingGlass, faTableColumns, faBell } from '@fortawesome/free-solid-svg-icons';
import {Advisor} from '../../models/advisor.model';
import {AuthService} from '../../../shared/auth/auth.service';
library.add(faEnvelope, faCalendarAlt, faMagnifyingGlass, faTableColumns, faBell);

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [FontAwesomeModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  // ======================================================== //
  // ======================== FIELDS ======================== //
  // ======================================================== //
  /* Current advisor */
  @Input() currentAdvisor!: Advisor | undefined;
  /* Is user menu open */
  isUserMenuOpen = false;

  // ======================================================== //
  // ======================= CONSTRUCTOR ===================== //
  // ======================================================== //
  /**
   * Header component constructor
   * @param authService
   */
  constructor(private authService: AuthService) {}

  // ======================================================== //
  // ======================== METHODS ======================== //
  // ======================================================== //
  /**
   * Toggle user menu visibility
   */
  toggleUserMenu() {
    this.isUserMenuOpen = !this.isUserMenuOpen;
  }

  /**
   * Close user menu when clicking outside
   * @param event
   */
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const userElement = document.querySelector('.user');
    if (userElement && !userElement.contains(event.target as Node)) {
      this.isUserMenuOpen = false;
    }
  }

  /**
   * Logout user
   */
  logout() {
    this.authService.logout();
  }
}
