import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BlocService {

  private baseUrl = 'http://192.168.33.10:8089/tpfoyer/bloc';

  constructor(private http: HttpClient) { }

  // Retrieve all blocs
  getBlocs(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/retrieve-all-blocs`);
  }



  // Add a new bloc
  addBloc(bloc: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/add-bloc`, bloc);
  }

  // Remove a bloc by ID
  removeBloc(bId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/remove-bloc/${bId}`);
  }

  // Modify an existing bloc
  modifyBloc(bloc: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/modify-bloc`, bloc);
  }



}
