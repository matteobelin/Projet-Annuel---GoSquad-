import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from '../../api.service';
import { Voyage } from '../models';

export interface VoyageCreationRequest {
  title: string;
  description?: string;
  startDate: string;
  endDate: string;
  destination: string;
  budget?: number;
  participantIds: number[];
  groupName?: string;
  selectedGroupId?: number | null;  // Permettre null explicitement
}

export interface VoyageApiResponse {
  uniqueTravelId: string;
  title: string;
  destination: string;
  startDate: string;
  endDate: string;
  budget: number;
  groups?: Array<{
    id?: number;
    name?: string;
    visible?: boolean;
    createdAt?: string;
    updatedAt?: string;
  }>;
  participants?: Array<{
    uniqueCustomerId: string;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber?: string;
    birthDate?: string;
    companyCode?: string;
  }>;
}

@Injectable({
  providedIn: 'root'
})
export class VoyageService {
  constructor(private apiService: ApiService) {}

  /**
   * Get all voyages
   * @returns Observable of VoyageApiResponse array
   */
  getAllVoyages(): Observable<VoyageApiResponse[]> {
    return this.apiService.get<VoyageApiResponse[]>('/getAllTravels');
  }

  /**
   * Get voyage by ID
   * @param id Voyage ID (uniqueTravelId string)
   * @returns Observable of Voyage
   */
  getVoyageById(id: string): Observable<Voyage> {
    return this.apiService.get<Voyage>(`/getTravel?id=${id}`);
  }

  /**
   * Create a new voyage with the new wizard format
   * @param request Voyage creation request
   * @returns Observable of success message
   */
  createVoyage(request: VoyageCreationRequest): Observable<string> {
    return this.apiService.postText('/travel', request);
  }

  /**
   * Update an existing voyage
   * @param id Voyage ID (uniqueTravelId string)
   * @param voyage Voyage data
   * @returns Observable of updated Voyage
   */
  updateVoyage(id: string, voyage: Partial<Voyage>): Observable<any> {
    return this.apiService.putText(`/travel/${id}`, voyage).pipe(
      catchError(error => {
        console.error('Erreur détaillée de la mise à jour du voyage:', error);
        // Vous pouvez choisir de renvoyer une erreur plus conviviale ou l'erreur d'origine
        return throwError(() => new Error('Une erreur est survenue lors de la mise à jour. Détails dans la console.'));
      })
    );
  }

  /**
   * Delete a voyage
   * @param id Voyage ID (uniqueTravelId string)
   * @returns Observable of operation result
   */
  deleteVoyage(id: string): Observable<any> {
    return this.apiService.delete<any>(`/travel/${id}`);
  }
}
