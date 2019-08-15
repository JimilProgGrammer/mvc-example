import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { RequestOptions, Headers, Http, Response } from '@angular/http';
import { Env } from '../models/env.prod';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ApiCallerService {

  private baseUrl: string = Env.host;

  constructor(private http: Http, private client: HttpClient, private auth: AuthService) { }

  doAuthorizedGet(url:string): Observable<any> {
    url = this.baseUrl + url;
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json; charset=utf-8').set('x-auth-token','BEARER '+ this.auth.getToken());
    return this.client.get(url, {headers: headers}).pipe(map((res:Response) => res));
  }

  doPostRequest(url:string, postBody: any):Observable<any> {
    let options: RequestOptions = new RequestOptions({
      headers: new Headers({ 'Content-Type': 'application/json' })
    });
    url = this.baseUrl + url;
    return this.http.post(url, postBody, options).pipe(map((res:Response) => res.json()));
  }

  doAuthorizedPost(url:string, postBody:any):Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json; charset=utf-8').set('x-auth-token','BEARER '+ this.auth.getToken());
    url = this.baseUrl + url;
    return this.client.post(url, postBody, {headers: headers}).pipe(map((res:Response) => res));
  }

}
