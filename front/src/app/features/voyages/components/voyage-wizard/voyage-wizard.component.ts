// ...existing code...
// ...existing code...
import { Component, OnInit, Output, EventEmitter, ChangeDetectorRef, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, Subject, debounceTime, distinctUntilChanged, switchMap, of, map, catchError, tap } from 'rxjs';
// Angular Material modules
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';

import { CustomerService, CustomerApiResponse } from '../../../../core/services/customer.service';
import { VoyageService, VoyageCreationRequest } from '../../../../core/services/voyage.service';
import { Customer } from '../../../../core/models/customer.model';
import { Group, GroupService } from '../../../../core/services/group.service';

export interface Participant {
  id: string;
  nom: string;
  prenom: string;
  email: string;
  displayId: string; // ID affiché à l'utilisateur (ex: "C001")
}

@Component({
  selector: 'app-voyage-wizard',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    // Angular Material modules
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatIconModule
  ],
  templateUrl: './voyage-wizard.component.html',
  styleUrls: ['./voyage-wizard.component.css']
})
export class VoyageWizardComponent implements OnInit {
  @Output() voyageCreated = new EventEmitter<void>();
  
  currentStep = 1;
  maxSteps = 3;

  // Forms pour chaque étape
  generalInfoForm!: FormGroup;
  participantsForm!: FormGroup;
  summaryForm!: FormGroup;

  // Données pour la recherche de participants
  searchTerm$ = new Subject<string>();
  searchResults: Participant[] = [];
  isSearching = false;
  selectedParticipants: Participant[] = [];
  
  // Données pour les groupes existants
  existingGroups: Group[] = [];
  showGroupSelection = false;
  showGroupNameField = false;

  // Liste des membres du groupe sélectionné
  selectedGroupMembers: any[] = [];

  // Liste des membres du groupe sélectionné
// ...existing code...

  // État du wizard
  isLoading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private groupService: GroupService,
    private voyageService: VoyageService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.initializeForms();
    this.setupParticipantSearch();
    this.setupFormControlBinding();
    this.loadExistingGroups();
  }

  private initializeForms(): void {
    // Étape 1: Informations générales
    this.generalInfoForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      description: [''],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      destination: ['', [Validators.required]],
      budget: [null, [Validators.min(0)]]
    });

    // Étape 2: Participants et groupe
    this.participantsForm = this.fb.group({
      searchTerm: [''],
      selectedGroupId: [null],
      groupName: ['']
    });

    // Étape 3: Résumé (lecture seule)
    this.summaryForm = this.fb.group({});
  }

  private setupParticipantSearch(): void {
    console.log('🚀 setupParticipantSearch appelée');
    // Lier le FormControl searchTerm au Subject pour déclencher la recherche
    this.participantsForm.get('searchTerm')?.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(term => {
      console.log('📝 Terme de recherche changé:', term);
      if (term && term.length >= 2) {
        this.isSearching = true;
        this.searchParticipants(term).subscribe({
          next: (results) => {
            console.log('🎯 Mise à jour searchResults avec:', results.length, 'éléments');
            this.searchResults = results;
            this.isSearching = false; // CORRIGER: mettre false AVANT detectChanges
            console.log('🎯 searchResults après mise à jour:', this.searchResults);
            this.cdr.detectChanges(); // Forcer la détection de changement
          },
          error: (error) => {
            console.error('Erreur lors de la recherche de participants:', error);
            this.searchResults = [];
            this.isSearching = false;
          }
        });
      } else {
        this.searchResults = [];
        this.isSearching = false;
      }
    });
  }

  private setupFormControlBinding(): void {
    // Cette méthode n'est plus nécessaire car on gère tout dans setupParticipantSearch
  }

  private searchParticipants(term: string): Observable<Participant[]> {
    console.log('🔍 Recherche participants avec terme:', term);
    return this.customerService.getAllCustomers().pipe(
      tap(customers => console.log('📋 Customers reçus de l\'API:', customers?.length || 0, customers)),
      map((customers: CustomerApiResponse[]) => {
        if (!customers || !Array.isArray(customers)) {
          console.warn('⚠️ Réponse API invalide:', customers);
          return [];
        }

        const filtered = customers
          .filter((customer: CustomerApiResponse) => {
            const firstName = customer.firstName || '';
            const lastName = customer.lastName || '';
            const email = customer.email || '';
            const customerId = customer.uniqueCustomerId || '';
            
            const searchTerm = term.toLowerCase();
            return firstName.toLowerCase().includes(searchTerm) ||
                   lastName.toLowerCase().includes(searchTerm) ||
                   email.toLowerCase().includes(searchTerm) ||
                   customerId.toLowerCase().includes(searchTerm);
          })
          .map((customer: CustomerApiResponse) => {
            // Utiliser directement l'ID du backend comme displayId
            const participant: Participant = {
              id: customer.uniqueCustomerId || '',
              nom: customer.lastName || '',
              prenom: customer.firstName || '',
              email: customer.email || '',
              displayId: customer.uniqueCustomerId || '' // Utiliser l'ID backend directement
            };
            
            console.log(`🎯 Customer transformé:`, customer, '→', participant);
            return participant;
          })
          .slice(0, 10); // Limiter à 10 résultats
        
        console.log('🎯 Participants filtrés:', filtered.length, filtered);
        return filtered;
      }),
      catchError((error: any) => {
        console.error('❌ Erreur lors de la recherche de participants:', error);
        return of([]);
      })
    );
  }

  private loadExistingGroups(): void {
    this.groupService.getAllGroups().subscribe({
      next: (groups) => {
        this.existingGroups = groups || [];
      },
      error: (err) => {
        console.error('Erreur lors du chargement des groupes existants :', err);
        this.existingGroups = [];
      }
    });
  }

  private isCurrentStepValid(): boolean {
    switch (this.currentStep) {
      case 1:
        return this.generalInfoForm.valid;
      case 2:
        return this.selectedParticipants.length > 0 && this.isGroupConfigurationValid();
      case 3:
        return true;
      default:
        return false;
    }
  }

  private isGroupConfigurationValid(): boolean {
    if (this.selectedParticipants.length === 1) {
      return true; // Pas de configuration groupe nécessaire pour solo
    }

    const selectedGroupId = this.participantsForm.get('selectedGroupId')?.value;
    const groupName = this.participantsForm.get('groupName')?.value;

    return selectedGroupId !== null || (groupName && groupName.trim().length > 0);
  }

  private updateGroupFields(): void {
    const participantCount = this.selectedParticipants.length;
    
    if (participantCount >= 2) {
      this.showGroupSelection = true;
      this.showGroupNameField = !this.participantsForm.get('selectedGroupId')?.value;
      
      if (this.showGroupNameField) {
        this.participantsForm.get('groupName')?.setValidators([Validators.required]);
      } else {
        this.participantsForm.get('groupName')?.clearValidators();
      }
      this.participantsForm.get('groupName')?.updateValueAndValidity();
    } else {
      this.showGroupSelection = false;
      this.showGroupNameField = false;
      this.participantsForm.get('groupName')?.clearValidators();
      this.participantsForm.get('groupName')?.updateValueAndValidity();
    }
  }

  // Gestion des participants
  addParticipant(participant: Participant): void {
    if (!this.selectedParticipants.find(p => p.id === participant.id)) {
      this.selectedParticipants.push(participant);
      this.participantsForm.get('searchTerm')?.setValue('');
      this.searchResults = []; // Vider les résultats après sélection
      this.updateGroupFields();
    }
  }

  removeParticipant(participantId: string): void {
    this.selectedParticipants = this.selectedParticipants.filter(p => p.id !== participantId);
    this.updateGroupFields();
  }

  // Gestion des groupes
  onGroupSelectionChange(): void {
    const selectedGroupId = this.participantsForm.get('selectedGroupId')?.value;
    this.showGroupNameField = !selectedGroupId;

    if (selectedGroupId) {
      this.participantsForm.get('groupName')?.clearValidators();
      this.participantsForm.get('groupName')?.setValue('');
      // Charger les membres du groupe sélectionné
      this.groupService.getGroupMembers(Number(selectedGroupId)).subscribe({
        next: (members) => {
          this.selectedGroupMembers = members || [];
        },
        error: (err) => {
          console.error('Erreur lors du chargement des membres du groupe :', err);
          this.selectedGroupMembers = [];
        }
      });
    } else {
      this.participantsForm.get('groupName')?.setValidators([Validators.required]);
      this.selectedGroupMembers = [];
    }
    this.participantsForm.get('groupName')?.updateValueAndValidity();
  }

  // Soumission finale
  onSubmit(): void {
    if (!this.isCurrentStepValid()) {
      return;
    }

    if (this.isLoading) {
      return;
    }

    this.isLoading = true;
    this.error = null;

    const selectedGroupIdValue = this.participantsForm.get('selectedGroupId')?.value;
    const groupName = this.participantsForm.get('groupName')?.value?.trim() || '';
    const participantIds = this.selectedParticipants.map(p => {
      const numericPart = p.id.replace(/[^0-9]/g, '');
      const id = parseInt(numericPart);
      if (isNaN(id) || id === 0) {
        console.warn(`ID participant invalide: "${p.id}" pour ${p.prenom} ${p.nom}`);
      }
      return id;
    }).filter(id => !isNaN(id) && id > 0);

    // Si un groupe existant est sélectionné
    if (selectedGroupIdValue && selectedGroupIdValue !== '') {
      const finalSelectedGroupId = parseInt(selectedGroupIdValue);
      this.createVoyage(finalSelectedGroupId, groupName, participantIds);
      return;
    }

    // Si un nouveau groupe doit être créé
    if (groupName && participantIds.length > 0) {
      const payload = {
        name: groupName,
        participantIds: participantIds,
        visible: participantIds.length > 1 // visible si plusieurs participants
      };
      console.log('Payload envoyé à l’API /groups :', payload);
      this.groupService.createGroup(payload).subscribe({
        next: (group) => {
          console.log('🟢 Groupe créé, ID retourné par l’API :', group?.id, group);
          this.createVoyage(group.id, group.name, participantIds);
        },
        error: (error) => {
          this.isLoading = false;
          this.error = `Erreur lors de la création du groupe: ${error.status} - ${error.message}`;
        }
      });
      return;
    }

    // Cas solo sans groupe : on crée toujours un groupe même pour un seul participant
    const soloPayload = {
      name: groupName || 'Groupe Solo',
      participantIds: participantIds,
      visible: false // solo
    };
    console.log('Payload envoyé à l’API /groups (solo) :', soloPayload);
    this.groupService.createGroup(soloPayload).subscribe({
      next: (group) => {
        console.log('🟢 Groupe solo créé, ID retourné par l’API :', group?.id, group);
        this.createVoyage(group.id, group.name, participantIds);
      },
      error: (error) => {
        this.isLoading = false;
        this.error = `Erreur lors de la création du groupe: ${error.status} - ${error.message}`;
      }
    });
  }

  private createVoyage(groupId: number | null, groupName: string, participantIds: number[]): void {
    const request: VoyageCreationRequest = {
      title: this.generalInfoForm.get('title')?.value?.trim(),
      description: this.generalInfoForm.get('description')?.value?.trim() || '',
      startDate: this.generalInfoForm.get('startDate')?.value,
      endDate: this.generalInfoForm.get('endDate')?.value,
      destination: this.generalInfoForm.get('destination')?.value?.trim(),
      budget: this.generalInfoForm.get('budget')?.value || 0,
      participantIds: participantIds,
      groupName: groupName,
      selectedGroupId: groupId
    };

    this.voyageService.createVoyage(request).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.voyageCreated.emit();
        this.router.navigate(['/voyages'])
          .then((navigationSuccessful) => {
            if (!navigationSuccessful) {
              return this.router.navigateByUrl('/voyages');
            }
            return Promise.resolve(true);
          })
          .then((secondTry) => {
            if (secondTry === false) {
              window.location.href = '/voyages';
            }
          })
          .catch((navigationError) => {
            console.error('Erreur de navigation:', navigationError);
            window.location.href = '/voyages';
          });
      },
      error: (error) => {
        console.error('❌ Erreur complète:', error);
        console.error('❌ Statut HTTP:', error.status);
        console.error('❌ Message d\'erreur:', error.message);
        console.error('❌ Erreur du serveur:', error.error);
        console.error('❌ URL appelée:', error.url);
        this.isLoading = false;
        this.error = `Erreur lors de la création du voyage: ${error.status} - ${error.message}`;
      }
    });
  }

  // Méthodes utilitaires pour le template
  get isStep1(): boolean { return this.currentStep === 1; }
  get isStep2(): boolean { return this.currentStep === 2; }
  get isStep3(): boolean { return this.currentStep === 3; }

  get canProceedToNextStep(): boolean {
    return this.isCurrentStepValid();
  }

  trackByParticipant(index: number, participant: Participant): string {
    return participant.id;
  }

  get selectedGroupName(): string {
    const selectedGroupId = this.participantsForm.get('selectedGroupId')?.value;
    if (selectedGroupId) {
      const group = this.existingGroups.find(g => g.id === selectedGroupId);
      return group ? group.name : '';
    }
    return this.participantsForm.get('groupName')?.value || '';
  }

  // Getters pour les données du résumé
  get summaryData() {
    return {
      general: this.generalInfoForm.value,
      participants: this.selectedParticipants,
      groupInfo: {
        isNewGroup: !this.participantsForm.get('selectedGroupId')?.value,
        groupName: this.selectedGroupName,
        isSolo: this.selectedParticipants.length === 1
      }
    };
  }
  // Navigation entre les étapes du wizard
  previousStep(): void {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }

  nextStep(): void {
    if (this.currentStep < this.maxSteps && this.canProceedToNextStep) {
      this.currentStep++;
    }
  }
}
