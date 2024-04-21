import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from '../model/DataModel';

@Injectable({
  providedIn: 'root'
})
export class EditProfileService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getUsers(): Observable<User[]>{
    return this.http.get<User[]>(`${this.apiServerUrl}/getUsers`);
  }

  public updateUser(user: FormData): Observable<string>{
    return this.http.put<string>(`${this.apiServerUrl}/updateUser`, user);
  }
}
