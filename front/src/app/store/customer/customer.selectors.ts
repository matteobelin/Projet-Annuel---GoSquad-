import { createFeatureSelector, createSelector } from '@ngrx/store';
import { CustomerState } from './customer.reducer';

export const selectCustomerState = createFeatureSelector<CustomerState>('customer');

export const selectCustomers = createSelector(
  selectCustomerState,
  (state) => state.customers
);

export const selectCustomersLoading = createSelector(
  selectCustomerState,
  (state) => state.loading
);
