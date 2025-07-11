import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Observable, of, switchMap, tap, take } from 'rxjs';

import { Category } from '../../../core/models/category.model';
import { CategoryStore } from '../../../store/categories/category.store.service';

@Component({
  selector: 'app-add-update-category-modal',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
  templateUrl: './add-update-category.component.html',
  styleUrls: ['./add-update-category.component.css']
})
export class AddUpdateCategoryModalComponent implements OnInit {
  @Input() visible = false;
  @Input() mode: 'add' | 'edit' = 'add';
  @Output() close = new EventEmitter<void>();
  @Output() submit = new EventEmitter<Category>();

  categoryForm!: FormGroup;
  categories$!: Observable<Category[]>;
  cachedCategories: Category[] = [];

  constructor(
    private fb: FormBuilder,
    private categoryStore: CategoryStore
  ) {}

  ngOnInit(): void {
    this.categoryStore.loadCategories();
    this.categories$ = this.categoryStore.getCategories().pipe(
      tap(cats => this.cachedCategories = cats)
    );

    this.initForm();

    if (this.mode === 'edit') {
      this.setupEditModeLogic();
    }
  }

  initForm(): void {
    if (this.mode === 'edit') {
      this.categoryForm = this.fb.group({
        categoryId: [null, Validators.required],
        newCategoryName: ['', Validators.required],
      });
    } else {
      this.categoryForm = this.fb.group({
        categoryName: ['', Validators.required],
      });
    }
  }

  setupEditModeLogic(): void {
    this.categoryForm.get('categoryId')?.valueChanges.pipe(
      switchMap((selectedId: number) => {
        if (selectedId !== null) {
          return this.categories$.pipe(
            tap(categories => {
              const selected = categories.find(cat => cat.id === selectedId);
              this.categoryForm.get('newCategoryName')?.setValue(selected?.name || '');
            })
          );
        } else {
          this.categoryForm.get('newCategoryName')?.setValue('');
          return of([]);
        }
      })
    ).subscribe();
  }

  onSubmit(): void {
    if (this.categoryForm.valid) {
      if (this.mode === 'edit') {
        const selectedId = this.categoryForm.get('categoryId')?.value;
        const newName = this.categoryForm.get('newCategoryName')?.value;

        const selectedCategory = this.cachedCategories.find(cat => cat.id === selectedId);
        if (!selectedCategory) {
          console.error('Catégorie non trouvée');
          return;
        }

        const updatedCategory: Category = {
          id: selectedId,
          name: selectedCategory.name,
          newName: newName,
        };

        this.categoryStore.updateCategory(updatedCategory);
        this.submit.emit(updatedCategory);
        this.onClose();
      } else {
        const newCategoryName = this.categoryForm.get('categoryName')?.value;
        const newCategory: Category = { name: newCategoryName };

        this.categoryStore.createCategory(newCategory);
        this.submit.emit(newCategory);
        this.onClose();
      }
    } else {
      this.categoryForm.markAllAsTouched();
    }
  }

  onClose(): void {
    this.close.emit();
    this.categoryForm.reset();
  }

  isSubmitDisabled(): boolean {
    if (!this.categoryForm.valid) return true;

    if (this.mode === 'edit') {
      const selectedId = this.categoryForm.get('categoryId')?.value;
      const newName = this.categoryForm.get('newCategoryName')?.value?.trim();

      const selectedCategory = this.cachedCategories.find(cat => cat.id === selectedId);
      const currentName = selectedCategory?.name?.trim() || '';

      return !newName || newName === currentName;
    }

    const name = this.categoryForm.get('categoryName')?.value?.trim();
    return !name;
  }
}
