import { createAction, props } from '@ngrx/store';
import { Customer } from '../../core/models/customer.model';

export const loadCustomers = createAction(
  '[Customer] Load Customers'
);

export const loadCustomersSuccess = createAction(
  '[Customer] Load Customers Success',
  props<{ customers: Customer[] }>()
);

export const loadCustomersFailure = createAction(
  '[Customer] Load Customers Failure',
  props<{ error: any }>()
);

export const loadCustomer = createAction(
  '[Customer] Load Customer',
  props<{ id: string }>()
);

export const loadCustomerSuccess = createAction(
  '[Customer] Load Customer Success',
  props<{ customer: Customer }>()
);

export const loadCustomerFailure = createAction(
  '[Customer] Load Customer Failure',
  props<{ error: any }>()
);

export const anonymizeCustomer = createAction(
  '[Customer] Anonymize Customer',
  props<{ uniqueCustomerId: string }>()
);

export const anonymizeCustomerSuccess = createAction(
  '[Customer] Anonymize Customer Success',
  props<{ uniqueCustomerId: string }>()
);

export const anonymizeCustomerFailure = createAction(
  '[Customer] Anonymize Customer Failure',
  props<{ error: any }>()
);

export const createCustomer = createAction(
  '[Customer] Create Customer',
  props<{ formData: FormData  }>()
)

export const createCustomerSuccess = createAction(
  '[Customer] Create Customer Success',
  props<{ uniqueCustomerId: string }>()
);

export const createCustomerFailure = createAction(
  '[Customer] Create Customer Failure',
  props<{ error: any }>()
);

export const updateCustomer = createAction(
  '[Customer] Update Customer',
  props<{ customer: Customer }>()
);

export const updateCustomerSuccess = createAction(
  '[Customer] Update Customer Success',
  props<{ uniqueCustomerId: string }>()
);

export const updateCustomerFailure = createAction(
  '[Customer] Update Customer Failure',
  props<{ error: any }>()
);

export const updateCustomerPassport = createAction(
  '[Customer] Update Customer Passport',
  props<{formData: FormData}>()
);

export const updateCustomerPassportSuccess = createAction(
  '[Customer] Update Customer Passport Success',
  );

export const updateCustomerPassportFailure = createAction(
  '[Customer] Update Customer Passport Failure',
  props<{ error: any }>()
);

export const updateCustomerIdCard = createAction(
  '[Customer] Update Customer ID Card',
  props<{ formData: FormData }>()
);

export const updateCustomerIdCardSuccess = createAction(
  '[Customer] Update Customer ID Card Success',
);

export const updateCustomerIdCardFailure = createAction(
  '[Customer] Update Customer ID Card Failure',
  props<{ error: any }>()
);



