import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-customer-document-modal',
  standalone: true,
  imports: [NgIf],
  templateUrl: './customer-document-modal.component.html',
  styleUrls: ['./customer-document-modal.component.css']
})
export class CustomerDocumentModalComponent {
  @Input() imageUrl: string | null = null;
  @Input() title: string = 'Document';
  @Output() close = new EventEmitter<void>();

  onClose() {
    this.close.emit();
  }
}
