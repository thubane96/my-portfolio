import { Tweet, User } from '../model/DataModel';
import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  
  userId = new EventEmitter<number>();
  private apiServerUrl = environment.apiBaseUrl;
  message: string;

  constructor(private http: HttpClient) { }

  public saveTweet(tweet: FormData): Observable<string>{
    return this.http.post<string>(`${this.apiServerUrl}/saveTweet`, tweet);
  }

  public getTweets(): Observable<Tweet[]>{
    return this.http.get<Tweet[]>(`${this.apiServerUrl}/getTweets`);
  }

  public likeTweet(tweetId: number): Observable<string>{
    return this.http.get(`${this.apiServerUrl}/likeTweet/${tweetId}`, {responseType: 'text'});
  }

  public getUser(): Observable<User>{
    return this.http.get<User>(`${this.apiServerUrl}/getUser`);
  }

  public getUsers(): Observable<User[]>{
    return this.http.get<User[]>(`${this.apiServerUrl}/getUsers`);
  }

  public deleteTweet(tweetId: number): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/deleteTweet/${tweetId}`);
  }

  public follow(usernameOfUserToFollow: string): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/follow/${usernameOfUserToFollow}`);
  }

  public pinTweet(tweetId: number): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/pinTweet/${tweetId}`);
  }

  public addTweetToList(tweetId: number): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/addTweetToList/${tweetId}`);
  }

  public hideTweet(tweetId: number): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/hideTweet/${tweetId}`);
  }

  public mute(usernameToMute: string): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/mute/${usernameToMute}`);
  }

  public block(usernameToBlock: string): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/block/${usernameToBlock}`);
  }

  public bookmark(tweetId: number): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/bookmark/${tweetId}`);
  }

  public saveNotification(notification: FormData): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/saveNotification`);
  }

}
