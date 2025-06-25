import { Component, Signal, computed } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { Customer } from '../../../core/models/customer.model';
import { CustomerStoreService } from '../../../store/customer/customer.store.service';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { signal } from '@angular/core';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule],
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent {
  private customers: Signal<Customer[]>;
  filterText = signal('');

  constructor(private customerStore: CustomerStoreService) {
    this.customerStore.loadCustomers();
    this.customers = toSignal(this.customerStore.getCustomers(), { initialValue: [] });
  }

  filteredCustomers = computed(() => {
    const filter = this.filterText().toLowerCase();
    return this.customers().filter(customer =>
      (customer.uniqueCustomerId ?? '').toLowerCase().includes(filter) ||
      (customer.lastName ?? '').toLowerCase().includes(filter) ||
      (customer.firstName ?? '').toLowerCase().includes(filter) ||
      (customer.email ?? '').toLowerCase().includes(filter)
    );
  });

  trackByCustomerId(index: number, customer: Customer): string {
    return customer.uniqueCustomerId;
  }

  onViewCustomer(id: string) {
    console.log('Voir client', id);
  }

  onDeleteCustomer(id: string) {
    console.log('Supprimer client', id);
  }
}
