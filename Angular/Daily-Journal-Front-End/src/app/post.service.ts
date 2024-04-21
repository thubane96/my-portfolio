import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Post } from './post';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getPosts(): Observable<Post[]>{
    return this.http.get<Post[]>(`${this.apiServerUrl}/post/all`);
  }

  public addPost(post: Post): Observable<Post>{
    return this.http.post<Post>(`${this.apiServerUrl}/post/add`, post);
  }

  public updatePost(post: Post): Observable<Post>{
    return this.http.put<Post>(`${this.apiServerUrl}/post/update`, post);
  }

  public deletePost(postId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/post/delete/${postId}`);
  }
  
}
