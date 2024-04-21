import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Notifications } from '../model/DataModel';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getNotifications(): Observable<Notifications[]>{
    return this.http.get<Notifications[]>(`${this.apiServerUrl}/getNotifications`)
  }

  public openNotification(notificationId: number): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/openNotification/${notificationId}`);
  }
}
