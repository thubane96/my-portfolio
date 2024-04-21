import { Comment, User } from './../model/DataModel';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommentReplyService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getComment(commentId: number): Observable<Comment>{
    return this.http.get<Comment>(`${this.apiServerUrl}/getComment/${commentId}`);
  }

  public addCommentReply(commentReply: FormData): Observable<string>{
    return this.http.post<string>(`${this.apiServerUrl}/addCommentReply`, commentReply);
  }

  public getUsers(): Observable<User[]>{
    return this.http.get<User[]>(`${this.apiServerUrl}/getUsers`);
  }

  likeComment(commentId: number): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/likeComment/${commentId}`);
  }

  public likeCommentReply(commentReplyId: number): Observable<string>{
    return this.http.get<string>(`${this.apiServerUrl}/likeCommentReply/${commentReplyId}`)
  }


  public deleteCommentReply(data: FormData): Observable<string>{
    return this.http.post<string>(`${this.apiServerUrl}/deleteCommentReply`, data);
  }
}
