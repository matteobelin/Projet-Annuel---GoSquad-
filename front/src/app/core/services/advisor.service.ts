import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../api.service';
import { Advisor } from '../models';

@Injectable({
  providedIn: 'root'
})
export class AdvisorService {
  constructor(private apiService: ApiService) {}

  /**
   * Get all advisors
   * @returns Observable of Advisor array
   */
  getAllAdvisors(): Observable<Advisor[]> {
    return this.apiService.get<Advisor[]>('/getAllAdvisor');
  }

  /**
   * Get advisor by ID
   * @param id Advisor ID
   * @returns Observable of Advisor
   */
  getAdvisorById(id: number): Observable<Advisor> {
    return this.apiService.get<Advisor>(`/getAdvisor/${id}`);
  }
}
