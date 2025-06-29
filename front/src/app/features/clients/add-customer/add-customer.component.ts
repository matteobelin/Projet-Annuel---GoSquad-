import {Component, inject, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import {CustomerDocumentFormModalComponent} from '../customer-document-add/customer-document-add.component';
import { CommonModule } from '@angular/common';
import {Router} from '@angular/router';
import { CustomerStoreService } from '../../../store/customer/customer.store.service';

@Component({
  selector: 'app-add-customer',
  templateUrl: './add-customer.component.html',
  standalone: true,
  imports: [ReactiveFormsModule, CustomerDocumentFormModalComponent, CommonModule],
  styleUrls: ['./add-customer.component.css']
})
export class AddCustomerComponent implements OnInit {

  private router = inject(Router);

  showDocumentForm = false;
  documentType: 'idCard' | 'passport' = 'idCard';
  passportImage: File | null = null;
  idCardImage: File | null = null;
  showIdCardFields = false;
  showPassportFields = false;
  mode : 'add' | 'edit' = 'add';
  customerId: string | null = null;


  customerForm!: FormGroup;
  private customerStore = inject(CustomerStoreService);


  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    const state = window.history.state;

    if (!state || !state.uniqueCustomerId) {
      this.router.navigate(['/clients']);
      return;
    }

    this.mode = state.mode || 'add';

    this.customerId = state.uniqueCustomerId || null;

    if(state.idCardNumber.value !== null){
      this.showIdCardFields = true;
    }
    if(state.passportNumber.value !== null){
      this.showPassportFields = true;
    }

    this.customerForm = this.fb.group({
      firstName: [state.firstName || '', Validators.required],
      lastName: [ state.lastName || '', Validators.required],
      birthDate: [ state.birthDate || '', [
        Validators.required,
        Validators.pattern(/^\d{4}-\d{2}-\d{2}$/) // Format YYYY-MM-DD
      ]],
      isoNationality: [state.isoNationality ||'', [
        Validators.required,
        Validators.pattern(/^[A-Z]{2}$/), // Exactement 2 lettres majuscules
        Validators.maxLength(2)
      ]],
      email: [ state.email ||'', [Validators.required, Validators.email]],
      phoneNumber: [ state.phoneNumber || '', [
        Validators.required,
        Validators.pattern(/^\+?[1-9]\d{1,14}$/)
      ]],
      addressLine: [ state.addressLine ||'', Validators.required],
      cityName: [ state.cityName || '', Validators.required],
      postalCode: [ state.postalCode || '', Validators.required],
      isoCode: [ state.isoCode || '', [
        Validators.required,
        Validators.pattern(/^[A-Z]{2}$/), // Exactement 2 lettres majuscules
        Validators.maxLength(2)
      ]],
      addressLineBilling: [ state.addressLineBilling || '', Validators.required],
      cityNameBilling: [ state.cityNameBilling || '', Validators.required],
      postalCodeBilling: [ state.postalCodeBilling || '', Validators.required],
      isoCodeBilling: [ state.isoCodeBilling || '', [
        Validators.required,
        Validators.pattern(/^[A-Z]{2}$/), // Exactement 2 lettres majuscules
        Validators.maxLength(2)
      ]],
      idCardNumber: [ state.idCardNumber || { value: null, disabled: true }],
      idCardExpirationDate: [ state.idCardExpirationDate || { value: null, disabled: true }, [
        Validators.pattern(/^\d{4}-\d{2}-\d{2}$/) // Format YYYY-MM-DD
      ]],
      passportNumber: [ state.passportNumber || { value: null, disabled: true }],
      passportExpirationDate: [ state.passportExpirationDate || { value: null, disabled: true }, [
        Validators.pattern(/^\d{4}-\d{2}-\d{2}$/) // Format YYYY-MM-DD
      ]],
    });

    // Ajout du + obligatoire au début du numéro de téléphone
    this.customerForm.get('phoneNumber')?.valueChanges.subscribe(value => {
      if (value && !value.startsWith('+')) {
        this.customerForm.get('phoneNumber')?.setValue('+' + value.replace(/^\+*/, ''), { emitEvent: false });
      }
    });

  }

  onSubmit(): void {
    if (this.customerForm.valid) {
      let customer = this.customerForm.getRawValue();
      const formData = new FormData();

      if (this.mode === 'add') {
        formData.append('customer', new Blob([JSON.stringify(customer)], { type: 'application/json' }));
        if (this.idCardImage) {
          formData.append('idCard', this.idCardImage);
        }
        if (this.passportImage) {
          formData.append('passport', this.passportImage);
        }
        this.customerStore.createCustomer(formData);
      }

      else{
        customer = {
          customer,
          uniqueCustomerId: this.customerId,}
        this.customerStore.updateCustomer(customer);
      }

    }
  }

  onCancel(): void {
    this.router.navigate(['/clients']);
    this.customerForm.reset();
  }


  onAddIdCard() {
    this.documentType = 'idCard';
    this.showDocumentForm = true;
  }

  onAddPassport() {
    this.documentType = 'passport';
    this.showDocumentForm = true;
  }


  onDocumentSubmit(event: { number: string; expiryDate: string; image: File | null }) {
    if(event.number){
      this.showDocumentForm = false;
      if (this.documentType === 'idCard') {
        this.customerForm.patchValue({idCardNumber: event.number , idCardExpirationDate: event.expiryDate});
        this.showIdCardFields = true;
        this.idCardImage = event.image;

        if (this.mode === 'edit') {
          const customer = {
            uniqueCustomerId: this.customerId,
            idCardNumber: event.number,
            idCardExpirationDate: event.expiryDate
          };
          const formData = new FormData();
          formData.append('customer', new Blob([JSON.stringify(customer)], { type: 'application/json' }));
          if (this.idCardImage) {
            formData.append('idCard', this.idCardImage);
          }
          this.customerStore.updateCustomerIdCard(formData);
        }

      }
      else if (this.documentType === 'passport') {
        this.customerForm.patchValue({passportNumber: event.number,passportExpirationDate: event.expiryDate});
        this.showPassportFields = true;
        this.passportImage = event.image;

        if (this.mode=== 'edit') {
          const customer = {
            uniqueCustomerId: this.customerId,
            passportNumber: event.number,
            passportExpirationDate: event.expiryDate
          };
          const formData = new FormData();
          formData.append('customer', new Blob([JSON.stringify(customer)], { type: 'application/json' }));
          if (this.passportImage) {
            formData.append('passport', this.passportImage);
          }
          this.customerStore.updateCustomerPassport(formData);
        }
      }
    }
  }

  onPhoneInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value
      .replace(/(?!^\+)[^\d]/g, '')  // Supprimer tout sauf chiffres (autorise + au début uniquement)
      .replace(/^(\+{2,})/, '+');    // Corrige plusieurs +
  }

}
