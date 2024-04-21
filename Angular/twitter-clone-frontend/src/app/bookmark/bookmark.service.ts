import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Tweet } from '../model/DataModel';

@Injectable({
  providedIn: 'root'
})
export class BookmarkService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getBookmarkedTweets(): Observable<Tweet[]>{
    return this.http.get<Tweet[]>(`${this.apiServerUrl}/getBookmarkedTweets`);
  }

}
