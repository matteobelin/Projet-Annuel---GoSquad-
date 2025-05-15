import {Injectable, OnInit} from '@angular/core';
import {ApiService} from '../../api.service';
import {AdvisorModel} from '../../model/advisor.model';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class AdvisorService implements OnInit{
  constructor(private apiService: ApiService, private http:HttpClient) {}

  private apiUrl = 'http://localhost:8080/getAllAdvisor';

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
