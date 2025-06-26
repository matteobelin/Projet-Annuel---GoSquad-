import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Advisor } from '../models';
import {ApiService} from '../../api.service';
import {AdvisorModel} from '../models/advisor.model';

@Injectable({
  providedIn: 'root'
})
export class AdvisorService {
  constructor(private apiService: ApiService) {}

  /**
   * Get all advisors
   * @returns Observable of Advisor array
   */
  getAllAdvisors(): Observable<AdvisorModel[]> {
    return this.apiService.get<AdvisorModel[]>('/getAllAdvisor');
  }

  /**
   * Get advisor by ID
   * @param id Advisor ID
   * @returns Observable of Advisor
   */
  getAdvisorById(id: number): Observable<AdvisorModel> {
    return this.apiService.get<AdvisorModel>(`/getAdvisor/${id}`);
  }
}
