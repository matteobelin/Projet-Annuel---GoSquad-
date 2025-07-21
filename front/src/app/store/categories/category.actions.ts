import { createAction, props } from '@ngrx/store';
import { Category } from '../../core/models/category.model';

export const loadCategories = createAction(
  '[Category] Load Categories'
);

export const loadCategoriesSuccess = createAction(
  '[Category] Load Categories Success',
  props<{ category: Category[] }>()
);

export const loadCategoriesFailure = createAction(
  '[Category] Load Category Failure',
  props<{ error: any }>()
);

export const loadCategory = createAction(
  '[Category] Load Category',
  props<{ id: string }>()
);

export const loadCategorySuccess = createAction(
  '[Customer] Load Category Success',
  props<{ category: Category }>()
);

export const loadCategoryFailure = createAction(
  '[Category] Load Category Failure',
  props<{ error: any }>()
);

export const loadCategoryByName = createAction(
  '[Category] Load Category By Name',
  props<{ name: string }>()
);

export const loadCategoryByNameSuccess = createAction(
  '[Category] Load Category By Name Success',
  props<{ category: Category }>()
);

export const loadCategoryByNameFailure = createAction(
  '[Category] Load Category By Name Failure',
  props<{ error: any }>()
);

export const createCategory = createAction(
  '[Category API] Create Category',
  props<{ category: Category }>()
);

export const createCategorySuccess = createAction(
  '[Category API] Create Category Success',
  props<{ category: Category }>()
);

export const createCategoryFailure = createAction(
  '[Category API] Create Category Failure',
  props<{ error: any }>()
);

// Update Category
export const updateCategory = createAction(
  '[Category API] Update Category',
  props<{ category: Category }>()
);

export const updateCategorySuccess = createAction(
  '[Category API] Update Category Success',
  props<{ update: Category }>()
);

export const updateCategoryFailure = createAction(
  '[Category API] Update Category Failure',
  props<{ error: any }>()
);


export const deleteCategory = createAction(
  '[Category API] Delete Category',
  props<{ id: number }>() // Change from string to number
);

export const deleteCategorySuccess = createAction(
  '[Category API] Delete Category Success',
  props<{ id: number }>() // Change from string to number
);

export const deleteCategoryFailure = createAction(
  '[Category API] Delete Category Failure',
  props<{ error: any }>()
);

