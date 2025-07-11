import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MainHeaderComponent } from "../../../presenter/main-header/main-header.component";
import { VoyageService } from '../../../../core/services/voyage.service';
import { ApiService } from '../../../../api.service';
import { CustomerService } from '../../../../core/services/customer.service';
import { Voyage } from '../../../../core/models';

@Component({
  selector: 'app-voyage-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MainHeaderComponent
  ],
  templateUrl: './voyage-edit.component.html',
  styleUrls: ['./voyage-edit.component.css']
})
export class VoyageEditComponent implements OnInit {
  editForm!: FormGroup;
  voyage: Voyage | null = null;
  loading = false;
  saving = false;
  error: string | null = null;
  voyageId: string = '';
  
  // Participant management properties
  searchTerm: string = '';
  searchResults: any[] = [];
  isSearching = false;
  searchTermControl: any;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private voyageService: VoyageService,
    private apiService: ApiService,
    private customerService: CustomerService,
    private cdr: ChangeDetectorRef
  ) {
    this.initializeForm();
    this.searchTermControl = this.fb.control('');
    this.setupParticipantSearch();
  }

  ngOnInit(): void {
    console.log('🚀 VoyageEditComponent ngOnInit called');
    this.route.params.subscribe(params => {
      console.log('📍 Route params:', params);
      this.voyageId = params['id'];
      console.log('🎯 Voyage ID extracted:', this.voyageId);
      if (this.voyageId) {
        console.log('✅ Calling loadVoyageForEdit...');
        this.loadVoyageForEdit();
      } else {
        console.log('❌ No voyage ID found in route params');
      }
    });
  }

  private initializeForm(): void {
    this.editForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: [''],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      destination: ['', [Validators.required, Validators.minLength(2)]],
      budget: [null, [Validators.min(0)]]
    });
  }

  loadVoyageForEdit(): void {
    console.log('🔄 loadVoyageForEdit started for voyageId:', this.voyageId);
    this.loading = true;
    this.error = null;

    this.voyageService.getVoyageById(this.voyageId).subscribe({
      next: (voyage) => {
        console.log('🔍 Voyage data received:', voyage);
        console.log('📊 Groups:', voyage.groups);
        console.log('👥 Participants:', voyage.participants);
        this.voyage = voyage;
        this.populateForm(voyage);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading voyage for edit:', error);
        this.error = 'Erreur lors du chargement du voyage';
        this.loading = false;
      }
    });
  }

  private populateForm(voyage: Voyage): void {
    this.editForm.patchValue({
      title: voyage.title,
      description: voyage.description || '',
      startDate: voyage.startDate ? this.formatDateForInput(voyage.startDate) : '',
      endDate: voyage.endDate ? this.formatDateForInput(voyage.endDate) : '',
      destination: voyage.destination,
      budget: voyage.budget
    });
  }

  private formatDateForInput(dateString: string): string {
    const date = new Date(dateString);
    return date.toISOString().split('T')[0];
  }

  onSubmit(): void {
    if (this.editForm.valid && !this.saving) {
      this.saving = true;
      this.error = null;

      const formData = this.editForm.value;
      const updatedVoyage: Partial<Voyage> = {
        title: formData.title,
        description: formData.description,
        startDate: formData.startDate,
        endDate: formData.endDate,
        destination: formData.destination,
        budget: formData.budget
      };

      this.voyageService.updateVoyage(this.voyageId, updatedVoyage).subscribe({
        next: (updatedVoyage) => {
          this.saving = false;
          // Rediriger vers la page de détail du voyage
          this.router.navigate(['/voyages', this.voyageId]);
        },
        error: (error) => {
          console.error('Error updating voyage:', error);
          this.error = 'Erreur lors de la mise à jour du voyage';
          this.saving = false;
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  private markFormGroupTouched(): void {
    Object.keys(this.editForm.controls).forEach(key => {
      const control = this.editForm.get(key);
      control?.markAsTouched();
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.editForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string {
    const field = this.editForm.get(fieldName);
    if (field && field.errors && (field.dirty || field.touched)) {
      const errors = field.errors;
      if (errors['required']) return `${fieldName} est requis`;
      if (errors['minlength']) return `${fieldName} doit contenir au moins ${errors['minlength'].requiredLength} caractères`;
      if (errors['min']) return `${fieldName} doit être positif`;
    }
    return '';
  }

  cancel(): void {
    this.router.navigate(['/voyages', this.voyageId]);
  }

  goBack(): void {
    this.router.navigate(['/voyages']);
  }

  // Participant management methods
  // Recherche réactive des clients (comme dans le wizard)
  private setupParticipantSearch(): void {
    this.searchTermControl.valueChanges
      .pipe(
        // Ajoute un délai pour éviter de spammer l'API
        (window as any).rxjs?.operators?.debounceTime ? (window as any).rxjs.operators.debounceTime(300) : (obs: any) => obs,
        (window as any).rxjs?.operators?.distinctUntilChanged ? (window as any).rxjs.operators.distinctUntilChanged() : (obs: any) => obs
      )
      .subscribe((value: any) => {
        const term = value as string;
        if (term && term.length >= 2) {
          this.isSearching = true;
          this.customerService.getAllCustomers().subscribe({
            next: (customers: any[]) => {
              const searchTermLower = term.toLowerCase();
              this.searchResults = customers.filter((customer: any) =>
                customer.firstName?.toLowerCase().includes(searchTermLower) ||
                customer.lastName?.toLowerCase().includes(searchTermLower) ||
                customer.email?.toLowerCase().includes(searchTermLower) ||
                customer.uniqueCustomerId?.toLowerCase().includes(searchTermLower)
              );
              this.isSearching = false;
              this.cdr.detectChanges();
            },
            error: () => {
              this.searchResults = [];
              this.isSearching = false;
              this.cdr.detectChanges();
            }
          });
        } else {
          this.searchResults = [];
          this.isSearching = false;
          this.cdr.detectChanges();
        }
      });
  }

  addParticipant(client: any): void {
    if (!this.voyage || !this.voyage.groups || this.voyage.groups.length === 0) {
      alert('Aucun groupe associé à ce voyage pour ajouter des participants');
      return;
    }

    const groupId = this.voyage.groups[0].id;
    
    // Extract numeric customer ID from uniqueCustomerId (remove GOSQUAD prefix)
    const numericCustomerId = client.uniqueCustomerId ? 
      parseInt(client.uniqueCustomerId.replace(/^GOSQUAD/, '')) : 
      client.id;

    if (!numericCustomerId || isNaN(numericCustomerId)) {
      alert('ID client invalide');
      return;
    }

    this.apiService.post(`/groups/${groupId}/add-customer`, { customerId: numericCustomerId }).subscribe({
      next: () => {
        // Reload voyage data to refresh participants list
        this.loadVoyageForEdit();
        this.searchTerm = '';
        this.searchResults = [];
      },
      error: (error) => {
        console.error('Error adding participant:', error);
        alert('Erreur lors de l\'ajout du participant');
      }
    });
  }

  removeParticipant(participant: any): void {
    if (!this.voyage || !this.voyage.groups || this.voyage.groups.length === 0) {
      alert('Aucun groupe associé à ce voyage');
      return;
    }

    if (confirm(`Êtes-vous sûr de vouloir retirer ${participant.firstName} ${participant.lastName} de ce voyage ?`)) {
      const groupId = this.voyage.groups[0].id;
      // Extract numeric customer ID from uniqueCustomerId
      const numericCustomerId = participant.uniqueCustomerId ? 
        parseInt(participant.uniqueCustomerId.replace(/^GOSQUAD/, '')) : 
        participant.id;

      if (!numericCustomerId || isNaN(numericCustomerId)) {
        alert('ID participant invalide');
        return;
      }
      this.apiService.delete(`/groups/${groupId}/remove-customer/${numericCustomerId}`).subscribe({
        next: () => {
          this.loadVoyageForEdit();
        },
        error: (error) => {
          console.error('Error removing participant:', error);
          alert('Erreur lors de la suppression du participant');
        }
      });
    }
  }

  isClientAlreadyParticipant(client: any): boolean {
    if (!this.voyage || !this.voyage.participants) {
      return false;
    }
    
    return this.voyage.participants.some(p => 
      p.uniqueCustomerId === client.uniqueCustomerId
    );
  }
}
