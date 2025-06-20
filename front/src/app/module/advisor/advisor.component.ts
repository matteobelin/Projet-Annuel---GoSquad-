import {Component, Signal} from '@angular/core';
import { FeatureNavComponent } from '../../core/components/feature-nav/feature-nav.component';
import { HeaderComponent } from '../../core/components/header/header.component';
import { RouterOutlet } from '@angular/router';
import { NgClass } from '@angular/common';
import { AdvisorStoreService } from '../../store/advisor/advisor.store.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { Advisor } from '../../core/models/advisor.model';

@Component({
  selector: 'app-advisor',
  standalone: true,
  imports: [FeatureNavComponent, HeaderComponent, RouterOutlet, NgClass],
  templateUrl: './advisor.component.html',
  styleUrl: './advisor.component.css'
})
export class AdvisorComponent{
  // ======================================================== //
  // ======================== FIELDS ======================== //
  // ======================================================== //
  /* Component title */
  title = 'front';
  /* Is sidebar collapsed */
  isCollapsed = false;
  /* Current advisor signal */
  currentAdvisor: Signal<Advisor | undefined>;

  // ======================================================== //
  // ======================= CONSTRUCTOR ===================== //
  // ======================================================== //
  /**
   * Advisor component constructor
   * @param advisorStoreService
   */
  constructor(private advisorStoreService: AdvisorStoreService) {
    // Initialize current advisor signal
    this.currentAdvisor = toSignal(
      this.advisorStoreService.getCurrentAdvisor(),
      { initialValue: undefined }
    );
  }
}
