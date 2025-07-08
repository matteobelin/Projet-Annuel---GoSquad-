import { Component, inject, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {Observable, tap, switchMap, of, take} from 'rxjs';

import { Category } from '../../../core/models/category.model';
import { CategoryStore } from '../../../store/categories/category.store.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-category-detail',
  templateUrl: './add-update-category.component.html',
  styleUrls: ['./add-update-category.component.css'],
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    FormsModule
  ]
})
export class AddUpdateCategoryComponent implements OnInit {
  cachedCategories: Category[] = [];
  categoryForm!: FormGroup;
  mode: 'add' | 'edit' = 'add';
  categories$!: Observable<Category[]>;
  private categoryStore = inject(CategoryStore);

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.categoryStore.loadCategories();
    this.categories$ = this.categoryStore.getCategories().pipe(
      tap(cats => this.cachedCategories = cats)
    );

    // Détermine le mode (add / edit)
    this.route.url.subscribe(segments => {
      this.mode = segments.some(seg => seg.path === 'edit') ? 'edit' : 'add';
      this.initForm();

      if (this.mode === 'edit') {
        this.setupEditModeLogic();
      }
    });
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

  /**
   * Gère la logique de remplissage du champ lors de la sélection d'une catégorie en mode édition.
   */
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

  /**
   * Gère la soumission du formulaire.
   */
  onSubmit(): void {
    if (this.categoryForm.valid) {
      if (this.mode === 'edit') {
        const selectedId = this.categoryForm.get('categoryId')?.value;
        const newName = this.categoryForm.get('newCategoryName')?.value;

        if (selectedId !== null && newName) {
          this.categories$.pipe(take(1)).subscribe(categories => {
            const selectedCategory = categories.find(cat => cat.id === selectedId);
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

            this.router.navigate(['/categories']); // Naviguer après la mise à jour
          });
        }
      } else {
        const newCategoryName = this.categoryForm.get('categoryName')?.value;
        if (newCategoryName) {
          const newCategory: Category = { name: newCategoryName };
          this.categoryStore.createCategory(newCategory);

          this.router.navigate(['/categories']); // Naviguer après la création
        }
      }
    } else {
      this.categoryForm.markAllAsTouched();
    }
  }

  onCancel(): void {
    this.router.navigate(['/categories']);
  }

  isSubmitDisabled(): boolean {
    if (!this.categoryForm.valid) return true;

    if (this.mode === 'edit') {
      const selectedId = this.categoryForm.get('categoryId')?.value;
      const newName = this.categoryForm.get('newCategoryName')?.value?.trim();

      let currentName = '';
      if (selectedId && this.categoryForm && this.categories$) {
        // Accès synchrone impossible → on garde l’état préenregistré
        const selectedCategory = this.cachedCategories?.find(cat => cat.id === selectedId);
        currentName = selectedCategory?.name?.trim() || '';
      }

      return !newName || newName === currentName;
    }

    // Mode "add"
    const name = this.categoryForm.get('categoryName')?.value?.trim();
    return !name;
  }
}
