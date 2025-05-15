import { Component, OnInit } from '@angular/core';
import { MainHeaderComponent } from "../presenter/main-header/main-header.component";
import { ClientListComponent } from "./presenters/client-list/client-list.component";
import { ClientService } from "../../core/services/client.service";
import { Client } from "../../core/models";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [
    CommonModule,
    MainHeaderComponent,
    ClientListComponent
  ],
  templateUrl: './clients.component.html',
  styleUrl: './clients.component.css'
})
export class ClientsComponent implements OnInit {
  clients: Client[] = [];
  loading = false;
  error: string | null = null;

  constructor(private clientService: ClientService) {}

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.loading = true;
    this.error = null;

    this.clientService.getAllClients().subscribe({
      next: (data) => {
        this.clients = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading clients', err);
        this.error = 'Erreur lors du chargement des clients. Veuillez réessayer.';
        this.loading = false;
      }
    });
  }

  handleViewClient(id: number): void {
    console.log('View client', id);
    // Implement view client logic (e.g., navigate to client details)
  }

  handleEditClient(id: number): void {
    console.log('Edit client', id);
    // Implement edit client logic (e.g., open edit form)
  }

  handleDeleteClient(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce client ?')) {
      this.clientService.deleteClient(id).subscribe({
        next: () => {
          this.clients = this.clients.filter(client => client.id !== id);
        },
        error: (err) => {
          console.error('Error deleting client', err);
          this.error = 'Erreur lors de la suppression du client. Veuillez réessayer.';
        }
      });
    }
  }
}
