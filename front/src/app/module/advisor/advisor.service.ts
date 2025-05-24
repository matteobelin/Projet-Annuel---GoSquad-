import {Injectable, OnInit} from '@angular/core';
import {AdvisorModel} from '../../model/advisor.model';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class AdvisorService implements OnInit{
  constructor(private http:HttpClient) {}

  private apiUrl = `${environment.apiUrl}/getAllAdvisor`;

  ngOnInit(): void {
    this.getAllAdvisors().subscribe(
      (response:AdvisorModel[])=>{
        console.log(response)
      }
    )
  }


  getAllAdvisors(): Observable<AdvisorModel[]> {
    return this.http.get<AdvisorModel[]>(this.apiUrl);
  }


}
