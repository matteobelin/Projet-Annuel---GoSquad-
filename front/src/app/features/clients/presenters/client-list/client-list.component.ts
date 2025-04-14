import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Client } from '../../../../core/models';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css']
})
export class ClientListComponent {
  @Input() clients: Client[] = [];
  @Output() viewClient = new EventEmitter<number>();
  @Output() editClient = new EventEmitter<number>();
  @Output() deleteClient = new EventEmitter<number>();

  onViewClient(id: number): void {
    this.viewClient.emit(id);
  }

  onEditClient(id: number): void {
    this.editClient.emit(id);
  }

  onDeleteClient(id: number): void {
    this.deleteClient.emit(id);
  }
}
