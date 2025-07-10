import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MainHeaderComponent } from "../../../presenter/main-header/main-header.component";
import { VoyageService } from '../../../../core/services/voyage.service';
import { Voyage } from '../../../../core/models';

@Component({
  selector: 'app-voyage-detail',
  standalone: true,
  imports: [
    CommonModule,
    MainHeaderComponent
  ],
  templateUrl: './voyage-detail.component.html',
  styleUrls: ['./voyage-detail.component.css']
})
export class VoyageDetailComponent implements OnInit {
  voyage: Voyage | null = null;
  loading = false;
  error: string | null = null;
  voyageId: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private voyageService: VoyageService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.voyageId = params['id'];
      if (this.voyageId) {
        this.loadVoyageDetail();
      }
    });
  }

  loadVoyageDetail(): void {
    console.log('🔍 Loading voyage detail for ID:', this.voyageId);
    this.loading = true;
    this.error = null;

    this.voyageService.getVoyageById(this.voyageId).subscribe({
      next: (voyage) => {
        console.log('🔍 Voyage detail data received:', voyage);
        console.log('📊 Groups:', voyage.groups);
        console.log('👥 Participants:', voyage.participants);
        this.voyage = voyage;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading voyage detail:', error);
        this.error = 'Erreur lors du chargement du voyage';
        this.loading = false;
      }
    });
  }

  editVoyage(): void {
    this.router.navigate(['/voyages', this.voyageId, 'edit']);
  }

  manageGroups(): void {
    this.router.navigate(['/voyages', this.voyageId, 'groups']);
  }

  deleteVoyage(): void {
    if (!this.voyage) return;

    if (confirm(`Êtes-vous sûr de vouloir supprimer le voyage "${this.voyage.title}" ?`)) {
      this.voyageService.deleteVoyage(this.voyageId).subscribe({
        next: () => {
          this.router.navigate(['/voyages']);
        },
        error: (error) => {
          console.error('Error deleting voyage:', error);
          alert('Erreur lors de la suppression du voyage');
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/voyages']);
  }

  calculateDuration(): number {
    if (!this.voyage?.startDate || !this.voyage?.endDate) return 0;
    
    const startDate = new Date(this.voyage.startDate);
    const endDate = new Date(this.voyage.endDate);
    const diffTime = Math.abs(endDate.getTime() - startDate.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    return diffDays;
  }

  trackByGroupId(index: number, groupe: any): any {
    return groupe.id || index;
  }

  trackByParticipantId(index: number, participant: any): any {
    return participant.id || index;
  }
}
