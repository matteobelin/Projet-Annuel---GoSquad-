import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
  FormsModule
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  Observable,
  Subject,
  of,
  takeUntil,
  switchMap,
  tap,
  take,
  filter
} from 'rxjs';
import { CommonModule } from '@angular/common';

import { Category } from '../../../core/models/category.model';
import { Activity } from '../../../core/models/activity.model';
import { CategoryStore } from '../../../store/categories/category.store.service';
import { ActivityStore } from '../../../store/activities/activity.store.service';

@Component({
  selector: 'app-activity-update-add',
  templateUrl: './add-update-activity.component.html',
  styleUrls: ['./add-update-activity.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule]
})
export class AddUpdateActivityComponent implements OnInit, OnDestroy {
  activityForm!: FormGroup;
  mode: 'add' | 'edit' = 'add';
  cachedCategories: Category[] = [];
  categories$!: Observable<Category[]>;
  originalActivity?: Activity;

  private destroy$ = new Subject<void>();
  private categoryStore = inject(CategoryStore);
  private activityStore = inject(ActivityStore);

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const segments = this.route.snapshot.url;
    this.mode = segments.some(seg => seg.path === 'edit') ? 'edit' : 'add';

    if (this.mode === 'edit') {
      this.handleEditMode();
    } else {
      this.handleAddMode();
    }
  }

  private handleEditMode(): void {
    this.route.paramMap
      .pipe(
        take(1),
        switchMap(params => {
          const id = Number(params.get('id'));
          if (!id || isNaN(id)) {
            console.error('ID manquant ou invalide dans l’URL');
            this.router.navigate(['/activities']);
            return of(null);
          }

          return this.activityStore.loadActivityById(id).pipe(
            switchMap(() =>
              this.activityStore.getSelectedActivity().pipe(
                filter(activity => !!activity),
                take(1))
            )
          );
        }),
        takeUntil(this.destroy$)
      )
      .subscribe(activity => {
        if (!activity) {
          console.error('Aucune activité trouvée pour cet ID');
          this.router.navigate(['/activities']);
          return;
        }

        // Appel de loadCategoriesWithSelection
        this.loadCategoriesWithSelection(activity.categoryName).subscribe(() => {
          // À ce stade, this.cachedCategories est à jour
          const selectedCategory = this.cachedCategories.find(
            cat => cat.name === activity.categoryName
          );

          // Ici on stocke originalActivity avec categoryId !
          this.originalActivity = {
            ...activity,
            categoryId: selectedCategory?.id ?? undefined
          };

          // Initialise le formulaire avec categoryId
          this.initForm({
            ...activity,
            categoryId: selectedCategory?.id
          });
        });
      });
  }

  private handleAddMode(): void {
    this.categoryStore.loadCategories();
    this.categories$ = this.categoryStore.getCategories().pipe(
      tap(cats => (this.cachedCategories = cats))
    );
    this.initForm();
  }

  private loadCategoriesWithSelection(categoryName: string): Observable<void> {
    // Charge d'abord toutes les catégories
    this.categoryStore.loadCategories();

    // Attend que les catégories soient chargées et non vides
    return this.categoryStore.getCategories().pipe(
      filter(categories => categories.length > 0),
      take(1),
      tap(categories => {
        this.cachedCategories = categories;

        const selectedCat = categories.find(cat => cat.name === categoryName);
        if (!selectedCat) {
          console.warn('Catégorie introuvable pour le nom :', categoryName);
          console.log('Liste des catégories chargées :', this.cachedCategories);
        } else {
          // Si catégorie trouvée, on peut éventuellement la mettre en premier dans la liste
          this.cachedCategories = [
            selectedCat,
            ...categories.filter(cat => cat.id !== selectedCat.id)
          ];
        }

        this.categories$ = of(this.cachedCategories);
      }),
      switchMap(() => of(void 0))
    );
  }

  initForm(activity?: Activity): void {
    this.activityForm = this.fb.group({
      activityName: [activity?.name || '', Validators.required],
      description: [activity?.description || '', Validators.required],
      categoryId: [activity?.categoryId ?? null, Validators.required],
      address: [activity?.address || '', Validators.required],
      city: [activity?.city || '', Validators.required],
      postalCode: [activity?.postalCode || '', Validators.required],
      country: [activity?.country || '', Validators.required],
      isoCode: [
        activity?.isoCode || '',
        [Validators.required, Validators.maxLength(2)]
      ],
      netPrice: [
        activity?.netPrice ?? null,
        [Validators.required, Validators.min(0)]
      ],
      vatRate: [
        activity?.vatRate ?? null,
        [Validators.required, Validators.min(0)]
      ]
    });
  }

  onSubmit(): void {
    if (!this.activityForm.valid) return;

    const selectedId = this.activityForm.get('categoryId')?.value;
    if (selectedId === null) return;

    this.categories$.pipe(take(1)).subscribe(categories => {
      const selectedCategory = categories.find(cat => cat.id === selectedId);

      if (!selectedCategory) {
        console.error('Catégorie non trouvée');
        return;
      }

      const formValue = this.activityForm.value;

      const activity: Activity = {
        id: this.originalActivity?.id,
        name: formValue.activityName,
        description: formValue.description,
        address: formValue.address,
        city: formValue.city,
        postalCode: formValue.postalCode,
        isoCode: formValue.isoCode,
        country: formValue.country,
        categoryName: selectedCategory.name,
        netPrice: formValue.netPrice,
        vatRate: formValue.vatRate
      };

      if (this.mode === 'add') {
        this.activityStore.createActivity(activity);
        this.activityStore.loadActivities()
      } else {
        this.activityStore.updateActivity(activity);
        this.activityStore.loadActivities()
      }

      this.router.navigate(['/activities']);
    });
  }

  onCancel(): void {
    this.router.navigate(['/activities']);
  }

  isSubmitDisabled(): boolean {
    if (!this.activityForm.valid) return true;

    if (this.mode === 'add') return false;

    if (this.originalActivity) {
      const formValue = this.activityForm.value;

      return (
        formValue.activityName === this.originalActivity.name &&
        formValue.description === this.originalActivity.description &&
        formValue.categoryId === this.originalActivity.categoryId &&
        formValue.address === this.originalActivity.address &&
        formValue.city === this.originalActivity.city &&
        formValue.postalCode === this.originalActivity.postalCode &&
        formValue.country === this.originalActivity.country &&
        formValue.isoCode === this.originalActivity.isoCode &&
        Number(formValue.netPrice) === Number(this.originalActivity.netPrice) &&
        Number(formValue.vatRate) === Number(this.originalActivity.vatRate)
      );
    }

    return true;
  }

  onIsoCodeInput(): void {
    const control = this.activityForm.get('isoCode');
    if (control) {
      const upperValue = control.value?.toUpperCase() || '';
      if (control.value !== upperValue) {
        control.setValue(upperValue, { emitEvent: false });
      }
    }
  }

  validateNumberInput(event: KeyboardEvent): void {
    const allowedKeys = [
      'Backspace',
      'Tab',
      'ArrowLeft',
      'ArrowRight',
      'Delete',
      'Home',
      'End',
      'Enter',
      '.',
      ...Array.from({ length: 10 }, (_, i) => i.toString())
    ];

    if (!allowedKeys.includes(event.key)) {
      event.preventDefault();
      return;
    }

    const input = event.target as HTMLInputElement;
    const value = input.value;
    const cursorPos = input.selectionStart || 0;
    const dotIndex = value.indexOf('.');

    if (event.key === '.' && dotIndex !== -1) {
      event.preventDefault();
      return;
    }

    if (dotIndex !== -1 && event.key >= '0' && event.key <= '9') {
      if (cursorPos > dotIndex) {
        const decimals = value.substring(dotIndex + 1);
        const selectedLength = (input.selectionEnd || 0) - cursorPos;
        const currentDecimalsCount = decimals.length - selectedLength;

        if (currentDecimalsCount >= 2) {
          event.preventDefault();
        }
      }
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
