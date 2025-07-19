import { Injectable } from '@angular/core';
import { Observable} from 'rxjs';
import { ApiService } from '../../api.service';

export interface TravelActivity {
  travelId : string ,
  activityId:number,
  startDate:string,
  endDate:string,
  groupId:number
}

export interface CustomerActivityResponse {
  uniqueCustomerId:string,
  participation:boolean,
  startDate:string,
  endDate:string,
  activityId:number,
  activityName:string,
  description:string,
  address:string,
  city:string,
  country:string,
  categoryName:string,
  netPrice:number,
  vatRate:number,
  gross_price:number
}


@Injectable({
  providedIn: 'root'
})
export class CustomerActivityService {
  constructor(private apiService: ApiService) {}

  getAllCustomerActivities(groupId: number): Observable<CustomerActivityResponse[]> {
    return this.apiService.get<CustomerActivityResponse[]>(`/customerActivity/by-group/participation?groupeId=${groupId}`);
  }

  createTravelActivity(request: TravelActivity): Observable<void> {
    return this.apiService.post<void>('/travel-activity/add', request);
  }

}
