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
    this.loading = true;
    this.error = null;

    this.voyageService.getVoyageById(this.voyageId).subscribe({
      next: (voyage) => {
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

  deleteVoyage(): void {
    if (!this.voyage) return;

    if (confirm(`Êtes-vous sûr de vouloir supprimer le voyage "${this.voyage.titre}" ?`)) {
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
}
