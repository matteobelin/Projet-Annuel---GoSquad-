import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { map, mergeMap, catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import * as CustomerActions from './customer.actions';
import { environment } from '../../../environments/environment';
import { Customer } from '../../core/models/customer.model';

@Injectable()
export class CustomerEffects {
  private readonly actions$ = inject(Actions);
  private getAllCustomersUrl = `${environment.apiUrl}/getAllCustomers`;
  private getCustomerUrl = `${environment.apiUrl}/getCustomer`;
  private anonymizeCustomerUrl = `${environment.apiUrl}/updateCustomerToAnonymous`;


  loadCustomers$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.loadCustomers),
      mergeMap(() =>
        this.http.get<Customer[]>(this.getAllCustomersUrl).pipe(
          map(customers => CustomerActions.loadCustomersSuccess({ customers })),
          catchError(error => of(CustomerActions.loadCustomersFailure({ error })))
        )
      )
    )
  );

  loadCustomer$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.loadCustomer),
      mergeMap(action =>
        this.http.get<Customer>(`${this.getCustomerUrl}?customerNumber=${action.id}`).pipe(
          map(customer => CustomerActions.loadCustomerSuccess({ customer })),
          catchError(error => of(CustomerActions.loadCustomerFailure({ error })))
        )
      )
    )
  );

  anonymizeCustomer$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.anonymizeCustomer),
      mergeMap(({ uniqueCustomerId }) =>
        this.http.put<void>(this.anonymizeCustomerUrl, { uniqueCustomerId }).pipe(
          map(() => CustomerActions.anonymizeCustomerSuccess({ uniqueCustomerId })),
          catchError(error => of(CustomerActions.anonymizeCustomerFailure({ error })))
        )
      )
    )
  );


  constructor(private http: HttpClient) {}
}
