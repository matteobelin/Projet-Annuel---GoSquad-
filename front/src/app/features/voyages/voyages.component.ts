import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MainHeaderComponent } from "../presenter/main-header/main-header.component";
import { VoyageWizardComponent } from './components/voyage-wizard/voyage-wizard.component';
import { VoyageService } from '../../core/services/voyage.service';
import { VoyageApiResponse } from '../../core/services/voyage.service';

@Component({
  selector: 'app-voyages',
  standalone: true,
  imports: [
    CommonModule,
    MainHeaderComponent,
    VoyageWizardComponent
  ],
  templateUrl: './voyages.component.html',
  styleUrl: './voyages.component.css'
})
export class VoyagesComponent implements OnInit {
  showWizard = false;
  voyages: VoyageApiResponse[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private voyageService: VoyageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadVoyages();
  }

  loadVoyages(): void {
    this.loading = true;
    this.error = null;
    
    this.voyageService.getAllVoyages().subscribe({
      next: (voyages) => {
        this.voyages = voyages;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading voyages:', error);
        this.error = 'Erreur lors du chargement des voyages';
        this.loading = false;
      }
    });
  }

  openWizard(): void {
    this.showWizard = true;
  }

  closeWizard(): void {
    this.showWizard = false;
    // Reload voyages when closing wizard to show any newly created voyage
    this.loadVoyages();
  }

  onWizardSuccess(): void {
    this.closeWizard();
  }

  viewVoyage(voyage: VoyageApiResponse): void {
    this.router.navigate(['/voyages', voyage.uniqueTravelId]);
  }

  editVoyage(voyage: VoyageApiResponse): void {
    this.router.navigate(['/voyages', voyage.uniqueTravelId, 'edit']);
  }

  manageGroups(voyage: VoyageApiResponse): void {
    this.router.navigate(['/voyages', voyage.uniqueTravelId, 'groups']);
  }

  deleteVoyage(voyage: VoyageApiResponse): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer le voyage "${voyage.title}" ?`)) {
      console.log('🗑️ Suppression du voyage demandé:', voyage);
      this.voyageService.deleteVoyage(voyage.uniqueTravelId).subscribe({
        next: () => {
          console.log('✅ Voyage supprimé avec succès:', voyage.uniqueTravelId);
          this.loadVoyages(); // Reload the list
        },
        error: (error) => {
          let errorMsg = 'Erreur lors de la suppression du voyage';
          if (error?.error) {
            errorMsg += ` : ${typeof error.error === 'string' ? error.error : JSON.stringify(error.error)}`;
          }
          console.error('❌', errorMsg, error);
        }
      });
    }
  }
}
