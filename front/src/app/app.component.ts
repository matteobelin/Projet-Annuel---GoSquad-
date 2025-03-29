import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {FeatureNavComponent} from './core/components/feature-nav/feature-nav.component';
import {HeaderComponent} from './core/components/header/header.component';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, FeatureNavComponent, HeaderComponent, NgClass],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'front';
  isCollapsed = false;
}
