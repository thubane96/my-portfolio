import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Message } from '../model/DataModel';

@Injectable({
  providedIn: 'root'
})
export class MessagesService {

  private apiServerUrl = environment.apiBaseUrl;

	constructor(private http: HttpClient) { }

  public getAllMessages(): Observable<Message[]>{
    return this.http.get<Message[]>(`${this.apiServerUrl}/getAllMessages`);
  }

  public openMessage(friendUsername: string): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/openMessage/${friendUsername}`);
  }
}
