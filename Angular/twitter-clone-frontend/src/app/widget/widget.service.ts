import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HomeService } from '../home/home.service';
import { Tweet, User } from '../model/DataModel';

@Injectable({
  providedIn: 'root'
})
export class WidgetService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getTweets(): Observable<Tweet[]>{
    return this.http.get<Tweet[]>(`${this.apiServerUrl}/getWidgetTweets`);
  }

  public likeTweet(tweetId: number): Observable<string>{
    return this.http.get(`${this.apiServerUrl}/likeTweet/${tweetId}`, {responseType: 'text'});
  }

  public getUsers(): Observable<User[]>{
    return this.http.get<User[]>(`${this.apiServerUrl}/getUsers`);
  }

  public follow(usernameOfUserToFollow: string): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/follow/${usernameOfUserToFollow}`);
  }
}
