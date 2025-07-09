import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../api.service';
import { Customer } from '../models/customer.model';

// Interface pour la réponse API réelle
export interface CustomerApiResponse {
  uniqueCustomerId: string;
  firstName: string;
  lastName: string;
  email: string;
}

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  constructor(private apiService: ApiService) {}

  getAllCustomers(): Observable<CustomerApiResponse[]> {
    return this.apiService.get<CustomerApiResponse[]>('/api/clients');
  }

  getCustomerById(id: string): Observable<Customer> {
    return this.apiService.get<Customer>(`/api/clients/${id}`);
  }
}
