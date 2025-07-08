import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Category } from '../../core/models/category.model'; // Assurez-vous que le chemin est correct

// Importation de toutes les actions et sélecteurs de catégorie
import * as CategoryActions from './category.actions';
import * as CategorySelectors from './category.selectors';
import { CategoryReducer } from './category.reducer'; // Importez l'interface de l'état de la catégorie

@Injectable({
  providedIn: 'root'
})
export class CategoryStore {
  constructor(private store: Store<CategoryReducer>) {} // Type the store with CategoryState


  loadCategories(): void {
    this.store.dispatch(CategoryActions.loadCategories());
  }


  getCategories(): Observable<Category[]> {
    return this.store.select(CategorySelectors.selectCategories);
  }

  loadCategory(id: string): void {
    this.store.dispatch(CategoryActions.loadCategory({ id }));
  }

  getSelectedCategory(): Observable<Category | null> {
    return this.store.select(CategorySelectors.selectSelectedCategory);
  }


  createCategory(category: Category): void {
    this.store.dispatch(CategoryActions.createCategory({ category }));
  }


  updateCategory(category: Category): void {
    this.store.dispatch(CategoryActions.updateCategory({ category }));
  }


  deleteCategory(id: number): void {
    this.store.dispatch(CategoryActions.deleteCategory({ id }));
  }

}
