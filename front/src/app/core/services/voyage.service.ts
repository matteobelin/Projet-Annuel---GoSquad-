import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../api.service';
import { Voyage } from '../models';

@Injectable({
  providedIn: 'root'
})
export class VoyageService {
  constructor(private apiService: ApiService) {}

  /**
   * Get all voyages
   * @returns Observable of Voyage array
   */
  getAllVoyages(): Observable<Voyage[]> {
    return this.apiService.get<Voyage[]>('/api/voyages');
  }

  /**
   * Get voyage by ID
   * @param id Voyage ID
   * @returns Observable of Voyage
   */
  getVoyageById(id: number): Observable<Voyage> {
    return this.apiService.get<Voyage>(`/api/voyages/${id}`);
  }

  /**
   * Create a new voyage
   * @param voyage Voyage data
   * @returns Observable of created Voyage
   */
  createVoyage(voyage: Omit<Voyage, 'id'>): Observable<Voyage> {
    return this.apiService.post<Voyage>('/api/voyages', voyage);
  }

  /**
   * Update an existing voyage
   * @param id Voyage ID
   * @param voyage Voyage data
   * @returns Observable of updated Voyage
   */
  updateVoyage(id: number, voyage: Partial<Voyage>): Observable<Voyage> {
    return this.apiService.put<Voyage>(`/api/voyages/${id}`, voyage);
  }

  /**
   * Delete a voyage
   * @param id Voyage ID
   * @returns Observable of operation result
   */
  deleteVoyage(id: number): Observable<any> {
    return this.apiService.delete<any>(`/api/voyages/${id}`);
  }
}
