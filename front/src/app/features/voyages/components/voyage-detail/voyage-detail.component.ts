import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MainHeaderComponent } from "../../../presenter/main-header/main-header.component";
import { VoyageService } from '../../../../core/services/voyage.service';
import { Voyage } from '../../../../core/models';
import {CustomerActivityService,CustomerActivityResponse} from '../../../../core/services/activityCustomer.service';

@Component({
  selector: 'app-voyage-detail',
  standalone: true,
  imports: [
    CommonModule,
    MainHeaderComponent,
    RouterModule
  ],
  templateUrl: './voyage-detail.component.html',
  styleUrls: ['./voyage-detail.component.css']
})
export class VoyageDetailComponent implements OnInit {
  voyage: Voyage | null = null;
  loading = false;
  error: string | null = null;
  voyageId: string = '';
  customerActivities: CustomerActivityResponse[] = []
  ActivityPrice= 0;
  accommodationPrice= 900;
  flightPrice= 1200;


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private voyageService: VoyageService,
    private activityCustomerService: CustomerActivityService
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

        if (voyage.groupId) {
          this.loadCustomerActivities(voyage.groupId);
        }
      },
      error: (error) => {
        console.error('Error loading voyage detail:', error);
        this.error = 'Erreur lors du chargement du voyage';
        this.loading = false;
      }
    });
  }

  loadCustomerActivities(groupId: number): void {
    this.activityCustomerService.getAllCustomerActivities(groupId).subscribe({
      next: (activities) => {
        this.customerActivities = activities;
        this.ActivityPrice = this.customerActivities
          .reduce((total, activity) => total + (activity.gross_price || 0), 0);
      },
      error: (error) => {
        console.error('Erreur lors du chargement des activités clients:', error);
      }
    });
  }

  editVoyage(): void {
    this.router.navigate(['/voyages', this.voyageId, 'edit']);
  }

  deleteVoyage(): void {
    if (!this.voyage) return;

    if (confirm(`Êtes-vous sûr de vouloir supprimer le voyage "${this.voyage.title}" ?`)) {
      this.voyageService.deleteVoyage(this.voyageId).subscribe({
        next: () => {
          this.router.navigate(['/voyages']);
        },
        error: (error) => {
          let errorMsg = 'Erreur lors de la suppression du voyage';
          if (error?.error) {
            errorMsg += ` : ${typeof error.error === 'string' ? error.error : JSON.stringify(error.error)}`;
          }
          console.error(errorMsg);
        }
      });
    }
  }

  addActivites(): void{
    if (!this.voyage) return
    this.router.navigate([
      '/activities',
      'travelActivity',
      this.voyageId,
      this.voyage.groupId,
      this.voyage.startDate,
      this.voyage.endDate,
      this.voyage.destination
    ]);
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

  async generatePdf(): Promise<void> {
    if (!this.voyage) {
      return;
    }

    const { default: jsPDF } = await import('jspdf');
    await import('jspdf-autotable');

    const doc = new jsPDF() as any;
    const voyage = this.voyage;

    // Titre
    doc.setFontSize(22);
    doc.text(`Détails du Voyage: ${voyage.title}`, 14, 20);
    doc.setFontSize(12);
    doc.text(`ID: ${voyage.uniqueTravelId}`, 14, 28);

    let y = 40;

    // Informations générales
    doc.setFontSize(16);
    doc.text('Informations Générales', 14, y);
    y += 8;
    doc.setFontSize(12);
    doc.text(`Destination: ${voyage.destination}`, 14, y);
    y += 7;
    doc.text(`Description: ${voyage.description || 'N/A'}`, 14, y);
    y += 7;
    // Format du budget compatible avec jsPDF
    const budget = voyage.budget ? `${voyage.budget.toFixed(2).replace('.', ',')} EUR` : 'N/A';
    doc.text(`Budget: ${budget}`, 14, y);
    y += 15;

    // Dates
    doc.setFontSize(16);
    doc.text('Dates', 14, y);
    y += 8;
    doc.setFontSize(12);
    const startDate = voyage.startDate ? new Date(voyage.startDate).toLocaleDateString('fr-FR') : 'N/A';
    doc.text(`Date de début: ${startDate}`, 14, y);
    y += 7;
    const endDate = voyage.endDate ? new Date(voyage.endDate).toLocaleDateString('fr-FR') : 'N/A';
    doc.text(`Date de fin: ${endDate}`, 14, y);
    y += 7;
    doc.text(`Durée: ${this.calculateDuration()} jours`, 14, y);
    y += 15;

    // Tableau des participants
    if (voyage.participants && voyage.participants.length > 0) {
      doc.setFontSize(16);
      doc.text('Participants', 14, y);
      y += 8;

      const head = [['ID Client', 'Prénom', 'Nom', 'Email']];
      const body = voyage.participants.map(p => [p.uniqueCustomerId || 'N/A', p.firstName, p.lastName, p.email]);

      doc.autoTable({
        startY: y,
        head: head,
        body: body,
        theme: 'striped',
        headStyles: { fillColor: [41, 128, 185] },
      });
      y = doc.lastAutoTable.finalY + 15;
    }

    // Tableau des groupes
    if (voyage.groups && voyage.groups.length > 0) {
      doc.setFontSize(16);
      doc.text('Groupes', 14, y);
      y += 8;

      const head = [['ID', 'Nom du groupe']];
      const body = voyage.groups.map(g => [g.id?.toString() || 'N/A', g.name || 'N/A']);

      doc.autoTable({
        startY: y,
        head: head,
        body: body,
        theme: 'striped',
        headStyles: { fillColor: [41, 128, 185] },
      });
      y = doc.lastAutoTable.finalY + 15;
    }

    if (this.customerActivities && this.customerActivities.length > 0) {
      doc.setFontSize(16);
      doc.text('Activités', 14, y);
      y += 8;

      const head = [['Nom', 'Ville', 'Pays','Date', 'Prix Unitaire TTC', 'Participants', "Prix Total TTC"]];

      const activityMap = new Map<string, { activity: CustomerActivityResponse, count: number }>();

      this.customerActivities.forEach(activity => {
        const key = `${activity.activityId}-${activity.startDate}`;
        if (activityMap.has(key)) {
          activityMap.get(key)!.count += 1;
        } else {
          activityMap.set(key, { activity, count: 1 });
        }
      });

      const body = Array.from(activityMap.values()).map(({ activity, count }) => [
        activity.activityName || 'N/A',
        activity.city || 'N/A',
        activity.country || 'N/A',
        activity.startDate ? new Date(activity.startDate).toLocaleDateString('fr-FR') : 'N/A',
        (activity.gross_price || 0).toFixed(2).replace('.', ','),
        count.toString(),
        (activity.gross_price*count || 0).toFixed(2).replace('.', ','),
      ]);

      doc.autoTable({
        startY: y,
        head: head,
        body: body,
        theme: 'striped',
        headStyles: { fillColor: [41, 128, 185] }, // Couleur différente pour différencier
      });

      y = doc.lastAutoTable.finalY + 15;
    }



    // Sauvegarder le PDF
    doc.save(`voyage-${voyage.uniqueTravelId}.pdf`);
  }

  trackByGroupId(index: number, groupe: any): any {
    return groupe.id || index;
  }

  trackByParticipantId(index: number, participant: any): any {
    return participant.id || index;
  }
}
