import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of, tap } from 'rxjs';
import { map, mergeMap, catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import * as CustomerActions from './customer.actions';
import { environment } from '../../../environments/environment';
import { Customer } from '../../core/models/customer.model';
import {Router} from '@angular/router';
import { CustomerStoreService } from './customer.store.service';

@Injectable()
export class CustomerEffects {
  private readonly actions$ = inject(Actions);
  private getAllCustomersUrl = `${environment.apiUrl}/api/clients`;
  private getCustomerUrl = `${environment.apiUrl}/api/clients`;
  private anonymizeCustomerUrl = `${environment.apiUrl}/updateCustomerToAnonymous`;
  private createCustomerUrl = `${environment.apiUrl}/customer`;
  private updateCustomerUrl = `${environment.apiUrl}/updateCustomer`;
  private updateCustomerPassportUrl = `${environment.apiUrl}/updateCustomerPassport`;
  private updateCustomerIdCardUrl = `${environment.apiUrl}/updateCustomerIdCard`;


  private readonly router = inject(Router);


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
      mergeMap(action => {
        // D'abord, vérifier si le customer existe dans la liste
        return this.http.get<any[]>(this.getAllCustomersUrl).pipe(
          mergeMap(customers => {
            const customerExists = customers.find(c => c.uniqueCustomerId === action.id);
            if (!customerExists) {
              throw new Error(`Customer avec l'ID ${action.id} non trouvé`);
            }
            
            // Si le customer existe, charger les détails complets via l'endpoint individuel
            return this.http.get<Customer>(`${this.getCustomerUrl}/${action.id}`).pipe(
              map(customer => CustomerActions.loadCustomerSuccess({ customer })),
              catchError(error => {
                // Si l'endpoint individuel échoue, on retourne au moins les données de base
                console.warn('Endpoint individuel échoué, utilisation des données de base:', error);
                const basicCustomer: Customer = {
                  uniqueCustomerId: customerExists.uniqueCustomerId,
                  firstName: customerExists.firstName,
                  lastName: customerExists.lastName,
                  email: customerExists.email
                };
                return of(CustomerActions.loadCustomerSuccess({ customer: basicCustomer }));
              })
            );
          }),
          catchError(error => {
            return of(CustomerActions.loadCustomerFailure({ error }));
          })
        );
      })
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

  createCustomer$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.createCustomer),
      mergeMap(({ formData }) =>
        this.http.post<{
          uniqueCustomerId: string }>(this.createCustomerUrl, formData).pipe(
          map(response =>
            CustomerActions.createCustomerSuccess({ uniqueCustomerId: response.uniqueCustomerId })
          ),
          catchError(error =>
            of(CustomerActions.createCustomerFailure({ error }))
          )
        )
      )
    )
  );

  createCustomerSuccess$ = createEffect(() =>
      this.actions$.pipe(
        ofType(CustomerActions.createCustomerSuccess),
        tap(({ uniqueCustomerId }) => {
          this.router.navigate(['/clients', uniqueCustomerId]);
        })
      ),
    { dispatch: false }
  );

  updateCustomer$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.updateCustomer),
      mergeMap(action => {
        return this.http.put(this.updateCustomerUrl, action.customer, { responseType: 'text' }).pipe(
          map(() =>
            CustomerActions.updateCustomerSuccess({ uniqueCustomerId: action.customer.uniqueCustomerId! })
          ),
          catchError(error => {
            return of(CustomerActions.updateCustomerFailure({ error }));
          })
        );
      })
    )
  );





  updateCustomerSuccess$ = createEffect(() =>
      this.actions$.pipe(
        ofType(CustomerActions.updateCustomerSuccess),
        tap(action => {
          this.customerStore.loadCustomer(action.uniqueCustomerId!);
          this.router.navigate(['/clients', action.uniqueCustomerId]);
        })
      ),
    { dispatch: false }
  );



  updateCustomerPassport$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.updateCustomerPassport),
      mergeMap(({ formData }) =>
        this.http.put<void>(this.updateCustomerPassportUrl, formData).pipe(
          map(() => CustomerActions.updateCustomerPassportSuccess()),
          catchError(error =>
            of(CustomerActions.updateCustomerPassportFailure({ error }))
          )
        )
      )
    )
  );

  updateCustomerIdCard$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.updateCustomerIdCard),
      mergeMap(({ formData }) =>
        this.http.put<void>(this.updateCustomerIdCardUrl, formData).pipe(
          map(() => CustomerActions.updateCustomerIdCardSuccess()),
          catchError(error =>
            of(CustomerActions.updateCustomerIdCardFailure({ error }))
          )
        )
      )
    )
  );


  constructor(private http: HttpClient,private customerStore: CustomerStoreService) {}
}
