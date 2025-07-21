import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-customer-document-add',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customer-document-add.component.html',
  styleUrls: ['./customer-document-add.component.css']
})
export class CustomerDocumentFormModalComponent {
  @Input() visible = false;
  @Input() title: string = 'Ajouter un document';
  @Output() close = new EventEmitter<void>();
  @Output() submit = new EventEmitter<{
    number: string;
    expiryDate: string;
    image: File | null;
  }>();

  documentForm: FormGroup;
  selectedImage: File | null = null;
  imagePreviewUrl: string | null = null;

  constructor(private fb: FormBuilder) {
    this.documentForm = this.fb.group({
      number: ['', Validators.required],
      expiryDate: ['', Validators.required],
      image: [null, Validators.required],
    });
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedImage = file;
      this.imagePreviewUrl = URL.createObjectURL(file);
      this.documentForm.patchValue({ image: file });
    }
  }

  onSubmit() {
    if (this.documentForm.valid) {
      this.submit.emit({
        number: this.documentForm.value.number,
        expiryDate: this.documentForm.value.expiryDate,
        image: this.selectedImage,
      });
      console.log('Form submitted:', this.documentForm.value);
      this.onClose();
    }
  }

  onClose() {
    this.close.emit();
    this.documentForm.reset();
    this.selectedImage = null;
    this.imagePreviewUrl = null;
  }





}
