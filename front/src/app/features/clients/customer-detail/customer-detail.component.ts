import { Component, Signal, OnInit, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { Customer } from '../../../core/models/customer.model';
import { CustomerStoreService } from '../../../store/customer/customer.store.service';
import { NgIf, NgFor } from '@angular/common';
import { CustomerDocumentModalComponent } from '../customer-document-modal/customer-document-modal.component';

@Component({
  selector: 'app-customer-detail',
  standalone: true,
  imports: [NgIf, NgFor, CustomerDocumentModalComponent],
  templateUrl: './customer-detail.component.html',
  styleUrls: ['./customer-detail.component.css'],
})
export class CustomerDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  selectedCustomer: Signal<Customer | null>;
  private customerId: string | null = null;


  showModal = false;
  modalImageUrl: string | null = null;
  modalTitle = '';


  constructor(private customerStore: CustomerStoreService) {
    this.selectedCustomer = toSignal(
      this.customerStore.getSelectedCustomer(),
      {initialValue: null}
    );
  }

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      this.customerId = params.get('id');
      const current = this.selectedCustomer();
      if (!current || current.uniqueCustomerId !== this.customerId) {
        if (this.customerId) {
          this.customerStore.loadCustomer(this.customerId);
        }
      }
    });
  }

  goBack() {
    this.router.navigate(['/clients']);
  }

  onEdit() {
    const customer = this.selectedCustomer();
    if (customer) {
      console.log('Modifier client:', customer.uniqueCustomerId);
      // this.router.navigate(['/clients', customer.uniqueCustomerId, 'edit']);
    }
  }

  onDelete() {
    const customer = this.selectedCustomer();
    if (customer && confirm(`Êtes-vous sûr de vouloir supprimer ${customer.firstName} ${customer.lastName} ?`)) {
      console.log('Supprimer client:', customer.uniqueCustomerId);
      // Implémenter la logique de suppression
      // this.goBack();
    }
  }

  viewIdCard() {
    const customer = this.selectedCustomer();
    if (customer?.idCard) {
      this.modalImageUrl ='data:image/png;base64,' + customer.idCard;
      this.modalTitle = 'Carte d\'identité';
      this.showModal = true;
    }

  }

  viewPassport() {
    const customer = this.selectedCustomer();
    if (customer?.passport) {
      this.modalImageUrl ='data:image/png;base64,' + customer.passport;
      this.modalTitle = 'Passeport';
      this.showModal = true;
    }
  }
  closeModal(){
    this.showModal = false;
    this.modalImageUrl = null;
    this.modalTitle = '';
  }
}
