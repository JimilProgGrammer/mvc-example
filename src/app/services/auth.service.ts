import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Env } from '../models/env.prod';

export const TOKEN_NAME: string = 'token';
export const USERNAME_KEY: string = "username";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private headers = new Headers({ 'Content-Type': 'application/json' });

  constructor(private http: Http) { }

  setCurrentUser(name: string) {
    localStorage.removeItem(USERNAME_KEY);
    localStorage.setItem(USERNAME_KEY, name);
  }

  getCurrentUser(): string {
    return localStorage.getItem(USERNAME_KEY);
  }

  getToken(): string {
    return localStorage.getItem(TOKEN_NAME);
  }

  setToken(token: string): void {
    localStorage.setItem(TOKEN_NAME, token);
  }

  logout() {
    localStorage.removeItem(TOKEN_NAME);
    localStorage.removeItem(USERNAME_KEY);
  }

  login(username, password): Observable<any> {
    return this.http
      .post(`${Env.host}/user/login`, { "username": username, "password": password }, { headers: this.headers })
      .pipe(map((res:Response) => res.json()));
  }

}
