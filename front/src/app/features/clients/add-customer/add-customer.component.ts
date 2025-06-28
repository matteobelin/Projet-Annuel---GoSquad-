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

  customerForm!: FormGroup;
  private customerStore = inject(CustomerStoreService);


  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    const state = window.history.state;
    this.customerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      birthDate: ['', [
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
      isoCode: [ state.postalCode || '', [
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
      idCardNumber: [{ value: null, disabled: true }],
      idCardExpirationDate: [{ value: null, disabled: true }, [
        Validators.pattern(/^\d{4}-\d{2}-\d{2}$/) // Format YYYY-MM-DD
      ]],
      passportNumber: [{ value: null, disabled: true }],
      passportExpirationDate: [{ value: null, disabled: true }, [
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
      const customer = this.customerForm.getRawValue();
      const formData = new FormData();
      formData.append('customer', new Blob([JSON.stringify(customer)], { type: 'application/json' }));
      if (this.idCardImage) {
        formData.append('idCard', this.idCardImage);
      }
      if (this.passportImage) {
        formData.append('passport', this.passportImage);
      }
      this.customerStore.createCustomer(formData);

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
      }
      else if (this.documentType === 'passport') {
        this.customerForm.patchValue({passportNumber: event.number,passportExpirationDate: event.expiryDate});
        this.showPassportFields = true;
        this.passportImage = event.image;
      }
    }
  }

}
