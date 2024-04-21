import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Message } from '../model/DataModel';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public saveMessage(messageData: FormData): Observable<string>{
    return this.http.post<string>(`${this.apiServerUrl}/saveMessage`, messageData);
  }

  public getMessages(): Observable<Message[]>{
    return this.http.get<Message[]>(`${this.apiServerUrl}/getAllMessages`);
  }
}
