import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {loadCustomers} from './customer.actions';
import {selectCustomers} from './customer.selectors';
import {Customer} from '../../core/models/customer.model';

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
}
