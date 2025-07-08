import { createReducer, on } from '@ngrx/store';
import * as CategoryActions from './category.actions';
import { Category } from '../../core/models/category.model'; // Ensure this import is correct for your Category interface

/**
 * L'interface décrivant la forme de l'état pour les catégories.
 */
export interface CategoryReducer {
  categories: Category[]; // This is the array holding all categories
  selectedCategory: Category | null; // For a single selected category
  loading: boolean;
  error: any;
}

/**
 * L'état initial pour le feature state 'category'.
 */
export const initialState: CategoryReducer = {
  categories: [],
  selectedCategory: null,
  loading: false,
  error: null
};

// @ts-ignore
/**
 * Le réducteur NgRx pour gérer l'état des catégories.
 */
export const categoryReducer = createReducer(
  initialState,

  // --- Charger toutes les catégories ---
  on(CategoryActions.loadCategories, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(CategoryActions.loadCategoriesSuccess, (state, { category }) => ({
    ...state,
    // CORRECTED: Assign the payload 'category' (which is Category[]) to state.categories
    categories: category,
    loading: false
  })),

  on(CategoryActions.loadCategoriesFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // --- Charger une seule catégorie ---
  on(CategoryActions.loadCategory, (state) => ({
    ...state,
    loading: true,
    selectedCategory: null,
    error: null
  })),

  on(CategoryActions.loadCategorySuccess, (state, { category }) => ({
    ...state,
    selectedCategory: category,
    loading: false
  })),

  on(CategoryActions.loadCategoryFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // --- Créer une catégorie ---
  on(CategoryActions.createCategory, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(CategoryActions.createCategorySuccess, (state, { category }) => ({
    ...state,
    categories: [...state.categories, category], // Ajoute la nouvelle catégorie à la liste
    loading: false
  })),

  on(CategoryActions.createCategoryFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // --- Mettre à jour une catégorie ---
  on(CategoryActions.updateCategory, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(CategoryActions.updateCategorySuccess, (state, { update }) => ({
    ...state,
    // Remplace la catégorie mise à jour dans la liste
    categories: state.categories.map(cat => cat.id === update.id ? update : cat),
    loading: false
  })),

  on(CategoryActions.updateCategoryFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // --- Supprimer une catégorie ---
  on(CategoryActions.deleteCategory, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(CategoryActions.deleteCategorySuccess, (state, { id }) => ({
    ...state,
    // CORRECTED: Filter state.categories (plural)
    // The (cat: Category) explicit type is fine, but not strictly needed here as TypeScript can infer it.
    categories: state.categories.filter((cat: Category) => cat.id !== id),
    loading: false
  })),

  on(CategoryActions.deleteCategoryFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);
