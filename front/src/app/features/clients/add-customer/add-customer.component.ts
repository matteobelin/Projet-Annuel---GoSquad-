import {Component, inject, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import {CustomerDocumentFormModalComponent} from '../customer-document-add/customer-document-add.component';
import { CommonModule } from '@angular/common';
import {Router} from '@angular/router';

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

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.customerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      birthDate: ['', Validators.required],
      isoNationality: ['', [Validators.required, Validators.maxLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [
        Validators.required,
        Validators.pattern(/^\+?[1-9]\d{1,14}$/)
      ]],
      addressLine: ['', Validators.required],
      cityName: ['', Validators.required],
      postalCode: ['', Validators.required],
      isoCode: ['', Validators.required],
      addressLineBilling: ['', Validators.required],
      cityNameBilling: ['', Validators.required],
      postalCodeBilling: ['', Validators.required],
      isoCodeBilling: ['', Validators.required],
      idCardNumber: [{ value: null, disabled: true }],
      idCardExpirationDate: [{ value: null, disabled: true }],
      passportNumber: [{ value: null, disabled: true }],
      passportExpirationDate: [{ value: null, disabled: true }],
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
      console.log('Formulaire envoyé :', this.customerForm.value);
      // Appel API ou service ici
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
