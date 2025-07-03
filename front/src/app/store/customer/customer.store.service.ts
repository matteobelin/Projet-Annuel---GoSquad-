import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { loadCustomers, loadCustomer,anonymizeCustomer, createCustomer, updateCustomer,updateCustomerIdCard,updateCustomerPassport } from './customer.actions';
import {selectCustomers, selectSelectedCustomer} from './customer.selectors';
import { Customer } from '../../core/models/customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerStoreService {
  constructor(private store: Store) {}

  loadCustomers(): void {
    this.store.dispatch(loadCustomers());
  }

  getCustomers(): Observable<Customer[]> {
    return this.store.select(selectCustomers);
  }

  loadCustomer(id: string): void {
    this.store.dispatch(loadCustomer({ id }));
  }

  getSelectedCustomer(): Observable<Customer | null> {
    return this.store.select(selectSelectedCustomer);
  }

  anonymizeCustomer(uniqueCustomerId: string): void {
    this.store.dispatch(anonymizeCustomer({ uniqueCustomerId }));
  }

  createCustomer(formData: FormData): void {
    this.store.dispatch(createCustomer({ formData }));
  }

  updateCustomer(customer: Customer): void {
    this.store.dispatch(updateCustomer({ customer }));
  }

  updateCustomerIdCard(formData: FormData): void {
    this.store.dispatch(updateCustomerIdCard({ formData }));
  }

  updateCustomerPassport(formData: FormData): void {
    this.store.dispatch(updateCustomerPassport({ formData }));
  }


}
