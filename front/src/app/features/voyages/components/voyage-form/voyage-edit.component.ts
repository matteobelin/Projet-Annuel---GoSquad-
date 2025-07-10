import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MainHeaderComponent } from "../../../presenter/main-header/main-header.component";
import { VoyageService } from '../../../../core/services/voyage.service';
import { Voyage } from '../../../../core/models';

@Component({
  selector: 'app-voyage-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
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

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private voyageService: VoyageService
  ) {
    this.initializeForm();
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

  manageParticipants(): void {
    // Navigate to participants management page
    this.router.navigate(['/voyages', this.voyageId, 'participants']);
  }

  manageGroups(): void {
    // Navigate to groups management page
    this.router.navigate(['/voyages', this.voyageId, 'groups']);
  }
}
