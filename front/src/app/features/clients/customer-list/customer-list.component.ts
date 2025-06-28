import {Component, Signal, computed, inject} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { Customer } from '../../../core/models/customer.model';
import { CustomerStoreService } from '../../../store/customer/customer.store.service';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { signal } from '@angular/core';
import {Router} from '@angular/router';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, ConfirmDialogComponent],
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent {

  selectedCustomerId: string | null = null;
  showConfirmDialog = false;
  confirmMessage = `Êtes-vous sûr de vouloir supprimer ce client ? Tapez "confirmer" pour valider.`;
  confirmText = 'confirmer';

  private customers: Signal<Customer[]>;
  filterText = signal('');
  private router = inject(Router);
  private removedCustomerIds = signal<Set<string>>(new Set());

  constructor(private customerStore: CustomerStoreService) {
    this.customerStore.loadCustomers();
    this.customers = toSignal(this.customerStore.getCustomers(), { initialValue: [] });
  }

  filteredCustomers = computed(() => {
    const filter = this.filterText().toLowerCase();
    const removedIds = this.removedCustomerIds();
    return this.customers().filter(customer =>
      customer.uniqueCustomerId && // on s’assure que l’id existe
      !removedIds.has(customer.uniqueCustomerId) &&
      (
        customer.uniqueCustomerId.toLowerCase().includes(filter) ||
        (customer.lastName ?? '').toLowerCase().includes(filter) ||
        (customer.firstName ?? '').toLowerCase().includes(filter) ||
        (customer.email ?? '').toLowerCase().includes(filter)
      )
    );
  });

  trackByCustomerId(index: number, customer: Customer): string {
    return customer.uniqueCustomerId ?? "";
  }

  onViewCustomer(id: string) {
    this.customerStore.loadCustomer(id);
    this.router.navigate(['/clients', id]);
  }

  onDeleteCustomer(id: string) {
    this.selectedCustomerId = id;
    this.showConfirmDialog = true;
  }

  onConfirmDelete() {
    this.showConfirmDialog = false;
    if (this.selectedCustomerId) {
      this.customerStore.anonymizeCustomer(this.selectedCustomerId);
      this.removedCustomerIds.update(set => {
        set.add(this.selectedCustomerId!);
        return set;
      });
      this.selectedCustomerId = null;
    }
    this.customerStore.loadCustomers();
  }

  onCancelDelete() {
    this.showConfirmDialog = false;
  }

  onAddCustomer() {
    this.router.navigate(['/clients', 'add']);
  }
}
