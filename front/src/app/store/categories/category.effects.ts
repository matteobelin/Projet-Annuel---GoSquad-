import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { map, mergeMap, catchError, tap } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

import * as CategoryActions from './category.actions';
import { environment } from '../../../environments/environment';
import { Category } from '../../core/models/category.model';

@Injectable()
export class CategoryEffects {
  private readonly actions$ = inject(Actions);
  private readonly http = inject(HttpClient);

  // API Endpoints based on your CategoryController
  private readonly apiUrl = environment.apiUrl;
  private readonly getAllCategoriesUrl = `${this.apiUrl}/category/all`;
  private readonly getCategoryByIdUrl = `${this.apiUrl}/category/by-id`;  // Assuming param is ?name=
  private readonly createCategoryUrl = `${this.apiUrl}/category`;
  private readonly updateCategoryUrl = `${this.apiUrl}/category`;
  private readonly deleteCategoryUrl = `${this.apiUrl}/category`;


  loadCategories$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CategoryActions.loadCategories),
      mergeMap(() =>
        this.http.get<Category[]>(this.getAllCategoriesUrl).pipe(
          map(categories => CategoryActions.loadCategoriesSuccess({ category: categories })),
          catchError(error => {
            console.error('[Effect] Error loading categories:', error);
            return of(CategoryActions.loadCategoriesFailure({ error }));
          })
        )
      )
    )
  );


  loadCategoryById$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CategoryActions.loadCategory),
      mergeMap(action =>
        this.http.get<Category>(`${this.getCategoryByIdUrl}?id=${action.id}`).pipe(
          map(category => CategoryActions.loadCategorySuccess({ category })),
          catchError(error => of(CategoryActions.loadCategoryFailure({ error })))
        )
      )
    )
  );


  createCategory$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CategoryActions.createCategory),
      mergeMap(action =>
        this.http.post<Category>(this.createCategoryUrl, action.category).pipe(
          map(createdCategory => CategoryActions.createCategorySuccess({ category: createdCategory })),
          catchError(error => of(CategoryActions.createCategoryFailure({ error })))
        )
      )
    )
  );



  updateCategory$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CategoryActions.updateCategory),
      mergeMap(action => {
        return this.http.put<Category>(this.updateCategoryUrl, action.category).pipe(
          map(updatedCategory => {
            return CategoryActions.updateCategorySuccess({ update: updatedCategory });
          }),
          catchError(error => {
            console.error('Erreur dans updateCategory:', error);
            return of(CategoryActions.updateCategoryFailure({ error }));
          })
        )
      })
    )
  );




  deleteCategory$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CategoryActions.deleteCategory),
      mergeMap(action => {
        const options = {
          body: {
            id: action.id // action.id is now correctly typed as number
            // Add other properties from CategoryRequestDTO if needed by the backend
          },
        };
        // Assuming your backend expects a number, it will handle the serialization.
        // If your delete endpoint uses a path variable like /category/{id},
        // you might need to adjust the URL: `${this.deleteCategoryUrl}/${action.id}`
        return this.http.delete<string>(this.deleteCategoryUrl, options).pipe(
          map(() => CategoryActions.deleteCategorySuccess({ id: action.id })),
          catchError(error => of(CategoryActions.deleteCategoryFailure({ error })))
        );
      })
    )
  );


}
