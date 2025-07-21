import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MainHeaderComponent } from "../../../presenter/main-header/main-header.component";
import { VoyageService } from '../../../../core/services/voyage.service';
import { ApiService } from '../../../../api.service';
import { Voyage } from '../../../../core/models';

interface Participant {
  id?: number;
  nom: string;
  prenom: string;
  email?: string;
  isSelected?: boolean;
}

interface Group {
  id?: number;
  nom: string;
  participants: Participant[];
  isEditing?: boolean;
  newName?: string;
}

@Component({
  selector: 'app-group-management',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MainHeaderComponent
  ],
  templateUrl: './group-management.component.html',
  styleUrls: ['./group-management.component.css']
})
export class GroupManagementComponent implements OnInit {
  voyage: Voyage | null = null;
  voyageId: string = '';
  loading = false;
  saving = false;
  error: string | null = null;
  
  // Groups and participants management
  groups: Group[] = [];
  availableParticipants: Participant[] = [];
  searchTerm = '';
  filteredParticipants: Participant[] = [];
  
  // New group creation
  showNewGroupForm = false;
  newGroupName = '';
  selectedParticipants: Participant[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private voyageService: VoyageService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.voyageId = params['id'];
      if (this.voyageId) {
        this.loadVoyageAndGroups();
        this.loadAvailableParticipants();
      }
    });
  }

  loadVoyageAndGroups(): void {
    this.loading = true;
    this.error = null;

    this.voyageService.getVoyageById(this.voyageId).subscribe({
      next: (voyage) => {
        this.voyage = voyage;
        this.initializeGroups(voyage);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading voyage:', error);
        this.error = 'Erreur lors du chargement du voyage';
        this.loading = false;
      }
    });
  }

  private initializeGroups(voyage: Voyage): void {
    this.groups = voyage.groupes?.map(group => ({
      id: group.id,
      nom: group.nom || 'Groupe sans nom',
      participants: group.participants || [],
      isEditing: false,
      newName: group.nom || ''
    })) || [];
  }

  loadAvailableParticipants(): void {
    // Load all customers as potential participants
    this.apiService.get<any[]>('/getAllCustomers').subscribe({
      next: (customers) => {
        this.availableParticipants = customers.map(customer => ({
          id: customer.id,
          nom: customer.nom,
          prenom: customer.prenom,
          email: customer.email,
          isSelected: false
        }));
        this.filteredParticipants = [...this.availableParticipants];
      },
      error: (error) => {
        console.error('Error loading participants:', error);
      }
    });
  }

  filterParticipants(): void {
    if (!this.searchTerm.trim()) {
      this.filteredParticipants = [...this.availableParticipants];
    } else {
      const term = this.searchTerm.toLowerCase();
      this.filteredParticipants = this.availableParticipants.filter(participant =>
        `${participant.prenom} ${participant.nom}`.toLowerCase().includes(term) ||
        participant.email?.toLowerCase().includes(term)
      );
    }
  }

  onSearchChange(): void {
    this.filterParticipants();
  }

  toggleParticipantSelection(participant: Participant): void {
    participant.isSelected = !participant.isSelected;
    
    if (participant.isSelected) {
      this.selectedParticipants.push(participant);
    } else {
      this.selectedParticipants = this.selectedParticipants.filter(p => 
        (p.id !== undefined && participant.id !== undefined && p.id !== participant.id) ||
        (p.id === undefined && participant.id === undefined && p.nom === participant.nom && p.prenom === participant.prenom)
      );
    }
  }

  showCreateGroupForm(): void {
    this.showNewGroupForm = true;
    this.newGroupName = '';
    this.selectedParticipants = [];
    this.availableParticipants.forEach(p => p.isSelected = false);
  }

  hideCreateGroupForm(): void {
    this.showNewGroupForm = false;
    this.newGroupName = '';
    this.selectedParticipants = [];
    this.availableParticipants.forEach(p => p.isSelected = false);
  }

  createNewGroup(): void {
    if (!this.newGroupName.trim()) {
      alert('Veuillez saisir un nom pour le groupe');
      return;
    }

    if (this.selectedParticipants.length === 0) {
      alert('Veuillez sélectionner au moins un participant');
      return;
    }

    const newGroup: Group = {
      nom: this.newGroupName.trim(),
      participants: [...this.selectedParticipants],
      isEditing: false
    };

    this.groups.push(newGroup);
    this.hideCreateGroupForm();
  }

  editGroupName(group: Group): void {
    group.isEditing = true;
    group.newName = group.nom;
  }

  saveGroupName(group: Group): void {
    if (group.newName && group.newName.trim()) {
      group.nom = group.newName.trim();
    }
    group.isEditing = false;
  }

  cancelEditGroupName(group: Group): void {
    group.isEditing = false;
    group.newName = group.nom;
  }

  removeParticipantFromGroup(group: Group, participant: Participant): void {
    if (confirm(`Retirer ${participant.prenom} ${participant.nom} du groupe "${group.nom}" ?`)) {
      group.participants = group.participants.filter(p => 
        (p.id !== undefined && participant.id !== undefined && p.id !== participant.id) ||
        (p.id === undefined && participant.id === undefined && (p.nom !== participant.nom || p.prenom !== participant.prenom))
      );
    }
  }

  deleteGroup(group: Group): void {
    if (confirm(`Supprimer le groupe "${group.nom}" ?`)) {
      this.groups = this.groups.filter(g => g !== group);
    }
  }

  addParticipantToGroup(group: Group, participant: Participant | null): void {
    if (!participant) return;
    
    // Check if participant is already in the group
    const isAlreadyInGroup = group.participants.some(p => 
      (p.id !== undefined && participant.id !== undefined && p.id === participant.id) ||
      (p.id === undefined && participant.id === undefined && p.nom === participant.nom && p.prenom === participant.prenom)
    );
    if (!isAlreadyInGroup) {
      group.participants.push({ ...participant });
    }
  }

  saveAllChanges(): void {
    this.saving = true;
    this.error = null;

    // Here you would typically call an API to save the group changes
    // For now, we'll simulate a save operation
    
    console.log('Saving groups:', this.groups);
    
    // Simulate API call
    setTimeout(() => {
      this.saving = false;
      alert('Groupes sauvegardés avec succès !');
    }, 1000);
  }

  goBack(): void {
    this.router.navigate(['/voyages', this.voyageId]);
  }

  getGroupParticipantCount(group: Group): number {
    return group.participants.length;
  }

  getTotalParticipants(): number {
    return this.groups.reduce((total, group) => total + group.participants.length, 0);
  }

  getUnassignedParticipants(): Participant[] {
    const assignedIds = new Set();
    const assignedNames = new Set();
    this.groups.forEach(group => {
      group.participants.forEach(participant => {
        if (participant.id !== undefined) {
          assignedIds.add(participant.id);
        } else {
          assignedNames.add(`${participant.nom}_${participant.prenom}`);
        }
      });
    });
    
    return this.availableParticipants.filter(participant => {
      if (participant.id !== undefined) {
        return !assignedIds.has(participant.id);
      } else {
        return !assignedNames.has(`${participant.nom}_${participant.prenom}`);
      }
    });
  }

  trackByGroupId(index: number, group: Group): any {
    return group.id || index;
  }

  trackByParticipantId(index: number, participant: Participant): any {
    return participant.id || index;
  }

  getParticipantFromSelect(participantId: string): Participant | null {
    const id = parseInt(participantId);
    return this.availableParticipants.find(p => p.id === id) || null;
  }
}
