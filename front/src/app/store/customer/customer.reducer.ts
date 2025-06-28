import { createReducer, on } from '@ngrx/store';
import * as CustomerActions from './customer.actions';
import { Customer } from '../../core/models/customer.model';

export interface CustomerState {
  customers: Customer[];
  selectedCustomer?: Customer | null;
  uniqueCustomerId?: string | null;
  loading: boolean;
  error: any;
}

export const initialState: CustomerState = {
  customers: [],
  selectedCustomer: null,
  uniqueCustomerId: null,
  loading: false,
  error: null
};

export const customerReducer = createReducer(
  initialState,

  // Chargement liste
  on(CustomerActions.loadCustomers, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(CustomerActions.loadCustomersSuccess, (state, { customers }) => ({
    ...state,
    customers,
    loading: false
  })),
  on(CustomerActions.loadCustomersFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  on(CustomerActions.loadCustomer, (state) => ({
    ...state,
    loading: true,
    selectedCustomer: null,
    error: null
  })),
  on(CustomerActions.loadCustomerSuccess, (state, { customer }) => ({
    ...state,
    selectedCustomer: customer,
    loading: false
  })),
  on(CustomerActions.loadCustomerFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  on(CustomerActions.anonymizeCustomerSuccess, (state, { uniqueCustomerId }) => ({
  ...state,
  customers: state.customers.filter(c => c.uniqueCustomerId !== uniqueCustomerId),
  error: null
})),

  on(CustomerActions.anonymizeCustomerFailure, (state, { error }) => ({
    ...state,
    error
  })),


on(CustomerActions.createCustomer, (state) => ({
  ...state,
  loading: true,
  error: null
})),

  on(CustomerActions.createCustomerSuccess, (state, { uniqueCustomerId }) => ({
    ...state,
    uniqueCustomerId,
    loading: false,
    error: null
  })),

  on(CustomerActions.createCustomerFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);

