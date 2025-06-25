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

export const selectSelectedCustomer = createSelector(
  selectCustomerState,
  (state) => state.selectedCustomer
);

export const selectCustomerError = createSelector(
  selectCustomerState,
  (state) => state.error
);
