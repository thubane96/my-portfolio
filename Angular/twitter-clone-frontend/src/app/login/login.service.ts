import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UserLogin, UserSignup } from '../model/DataModel';

@Injectable({
  providedIn: 'root'
})
export class LoginService {


	private apiServerUrl = environment.apiBaseUrl;

	constructor(private http: HttpClient) { }

	signin(userData: UserLogin): Observable<any> {
		return this.http.post<any>(`${this.apiServerUrl}/signin`, userData,
		{headers: new HttpHeaders({ 'Content-Type': 'application/json' })}).pipe(map((resp) => {
			sessionStorage.setItem('user', userData.username);
			sessionStorage.setItem('token', 'HTTP_TOKEN ' + resp.token);
			return resp;
		}));
	}

	signup(userData: UserSignup): Observable<any> {
		return this.http.post<any>(`${this.apiServerUrl}/signup`, userData, 
		{headers: new HttpHeaders({ 'Content-Type': 'application/json' }), 
		responseType: 'text' as 'json'}).pipe(map((resp) => {                                                         
			return resp;
		}));
	}


	signout() {
		sessionStorage.removeItem('user');
		sessionStorage.removeItem('token');
	}

	getByUserRole(): Observable<string>{
		return this.http.get<string>(`${this.apiServerUrl}/greet/user`,
			{responseType: 'text' as 'json'}).pipe(map((resp) => {
			return resp;
		}));
	}

	isUserSignedin(): boolean {
		return sessionStorage.getItem('token') !== null;
	}

	getSignedinUser(): string {
		let signedinUser: string = sessionStorage.getItem('user');
		return signedinUser;
	}

	getToken(): string {
		let token: string = sessionStorage.getItem('token');
		return token;
	}

	isTokenEmpty(): boolean {
		let str = this.getToken();
		return (!str || str.length === 0);
	}
}
