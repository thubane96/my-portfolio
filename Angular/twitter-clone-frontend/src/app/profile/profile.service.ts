import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Tweet, User } from '../model/DataModel';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getTweets(): Observable<Tweet[]>{
    return this.http.get<Tweet[]>(`${this.apiServerUrl}/getAllTweets`);
  }

  public getUsers(): Observable<User[]>{
    return this.http.get<User[]>(`${this.apiServerUrl}/getUsers`);
  }

  public likeTweet(tweetId: number): Observable<string>{
    return this.http.get(`${this.apiServerUrl}/likeTweet/${tweetId}`, {responseType: 'text'});
  }

  public getUser(): Observable<User>{
    return this.http.get<User>(`${this.apiServerUrl}/getUser`);
  }

  public deleteTweet(tweetId: number): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/deleteTweet/${tweetId}`);
  }

  likeComment(commentId: number): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/likeComment/${commentId}`);
  }

  public deleteComment(data: FormData): Observable<string>{
    return this.http.post<string>(`${this.apiServerUrl}/deleteComment`, data);
  }

  public likeCommentReply(commentReplyId: number): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/likeCommentReply/${commentReplyId}`)
  }


  public deleteCommentReply(data: FormData): Observable<string>{
    return this.http.post<string>(`${this.apiServerUrl}/deleteCommentReply`, data);
  }

  public follow(usernameOfUserToFollow: string): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/follow/${usernameOfUserToFollow}`);
  }
  
}
