import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { loadCustomers, loadCustomer,anonymizeCustomer  } from './customer.actions';
import { selectCustomers, selectSelectedCustomer } from './customer.selectors';
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
}
