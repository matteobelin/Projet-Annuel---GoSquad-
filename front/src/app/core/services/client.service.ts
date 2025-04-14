import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../api.service';
import { Client } from '../models';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  constructor(private apiService: ApiService) {}

  /**
   * Get all clients
   * @returns Observable of Client array
   */
  getAllClients(): Observable<Client[]> {
    return this.apiService.get<Client[]>('/api/clients');
  }

  /**
   * Get client by ID
   * @param id Client ID
   * @returns Observable of Client
   */
  getClientById(id: number): Observable<Client> {
    return this.apiService.get<Client>(`/api/clients/${id}`);
  }

  /**
   * Create a new client
   * @param client Client data
   * @returns Observable of created Client
   */
  createClient(client: Omit<Client, 'id'>): Observable<Client> {
    return this.apiService.post<Client>('/api/clients', client);
  }

  /**
   * Update an existing client
   * @param id Client ID
   * @param client Client data
   * @returns Observable of updated Client
   */
  updateClient(id: number, client: Partial<Client>): Observable<Client> {
    return this.apiService.put<Client>(`/api/clients/${id}`, client);
  }

  /**
   * Delete a client
   * @param id Client ID
   * @returns Observable of operation result
   */
  deleteClient(id: number): Observable<any> {
    return this.apiService.delete<any>(`/api/clients/${id}`);
  }
}
