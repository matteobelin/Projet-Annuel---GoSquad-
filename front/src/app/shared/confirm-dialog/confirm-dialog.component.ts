import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent {
  @Input() message = 'Êtes-vous sûr ?';
  @Input() confirmText = 'confirmer';

  inputText = '';
  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();


  checkInput() {
    if (this.inputText.toLowerCase() === this.confirmText.toLowerCase()) {
      setTimeout(() => {
        this.confirmed.emit();
      }, 500);
    }
  }

  onCancel() {
    this.cancelled.emit();
  }
}
