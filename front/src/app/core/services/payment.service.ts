import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../api.service';
import { Payment } from '../models';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  constructor(private apiService: ApiService) {}

  /**
   * Get all payments
   * @returns Observable of Payment array
   */
  getAllPayments(): Observable<Payment[]> {
    return this.apiService.get<Payment[]>('/api/payments');
  }

  /**
   * Get payment by ID
   * @param id Payment ID
   * @returns Observable of Payment
   */
  getPaymentById(id: number): Observable<Payment> {
    return this.apiService.get<Payment>(`/api/payments/${id}`);
  }

  /**
   * Create a new payment
   * @param payment Payment data
   * @returns Observable of created Payment
   */
  createPayment(payment: Omit<Payment, 'id'>): Observable<Payment> {
    return this.apiService.post<Payment>('/api/payments', payment);
  }

  /**
   * Update an existing payment
   * @param id Payment ID
   * @param payment Payment data
   * @returns Observable of updated Payment
   */
  updatePayment(id: number, payment: Partial<Payment>): Observable<Payment> {
    return this.apiService.put<Payment>(`/api/payments/${id}`, payment);
  }

  /**
   * Delete a payment
   * @param id Payment ID
   * @returns Observable of operation result
   */
  deletePayment(id: number): Observable<any> {
    return this.apiService.delete<any>(`/api/payments/${id}`);
  }
}
