import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {NgClass} from '@angular/common';
import {TokenExpirationService} from './core/interceptors/authExpiration.interceptor';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgClass],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  isCollapsed = false;
  constructor(private tokenExpirationService: TokenExpirationService) {}

  ngOnInit() {
    this.tokenExpirationService.startTokenExpirationCheck();
  }

  ngOnDestroy() {
    this.tokenExpirationService.stopTokenExpirationCheck();
  }
}
