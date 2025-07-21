import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../api.service';
import { Customer } from '../models/customer.model';

export interface Group {
  id: number;
  name: string;
  visible?: boolean;
  createdAt?: string;
  updatedAt?: string;
  memberCount?: number;
}

export interface CreateGroupRequest {
  name: string;
  participantIds: number[];
  visible?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  constructor(private apiService: ApiService) {}

  createGroup(request: CreateGroupRequest): Observable<Group> {
    return this.apiService.post<Group>('/groups', request);
  }

  getAllGroups(): Observable<Group[]> {
    return this.apiService.get<Group[]>('/groups');
  }

  getGroupById(id: number): Observable<Group> {
    return this.apiService.get<Group>(`/groups/${id}`);
  }

  getGroupMembers(groupId: number): Observable<Customer[]> {
    return this.apiService.get<Customer[]>(`/groups/${groupId}/members`);
  }
}
