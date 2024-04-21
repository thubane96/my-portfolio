import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Tweet, User } from '../model/DataModel';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  getTweet(tweetId: number): Observable<Tweet>{
    return this.http.get<Tweet>(`${this.apiServerUrl}/getTweet/${tweetId}`);
  }

  addComment(comment: FormData): Observable<string>{
    return this.http.post<string>(`${this.apiServerUrl}/addComment`, comment);
  }

  likeComment(commentId: number): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/likeComment/${commentId}`);
  }

  public getUsers(): Observable<User[]>{
    return this.http.get<User[]>(`${this.apiServerUrl}/getUsers`);
  }

  public likeTweet(tweetId: number): Observable<string>{
    return this.http.get(`${this.apiServerUrl}/likeTweet/${tweetId}`, {responseType: 'text'});
  }

  public deleteComment(data: FormData): Observable<string>{
    return this.http.post<string>(`${this.apiServerUrl}/deleteComment`, data);
  }
  
}
