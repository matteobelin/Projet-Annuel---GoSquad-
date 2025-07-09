import { createFeatureSelector, createSelector } from '@ngrx/store';
import { CategoryReducer } from './category.reducer'; // Assuming you have a category.reducer.ts
import { Category } from '../../core/models/category.model';

// Selects the 'category' feature state from the main store
export const selectCategoryState = createFeatureSelector<CategoryReducer>('category');

/**
 * Selects the list of all categories.
 */
export const selectCategories = createSelector(
  selectCategoryState,
  (state: CategoryReducer) => state.categories
);

/**
 * Selects the loading status for the category feature.
 */
export const selectCategoriesLoading = createSelector(
  selectCategoryState,
  (state: CategoryReducer) => state.loading
);

/**
 * Selects the currently selected category.
 */
export const selectSelectedCategory = createSelector(
  selectCategoryState,
  (state: CategoryReducer): Category | null => state.selectedCategory ?? null
);

/**
 * Selects any error related to the category feature.
 */
export const selectCategoryError = createSelector(
  selectCategoryState,
  (state: CategoryReducer) => state.error
);

export const selectCategoryByName = createSelector(
  selectCategoryState,
  (state: CategoryReducer) => state.selectedCategory // ou ce que tu veux
);
