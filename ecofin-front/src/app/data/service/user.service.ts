import {inject, Injectable, signal} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {User} from "../interface/user.interface";
import {tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  http = inject(HttpClient)
  baseApiUrl = environment.BASE_BACKEND_URL + "users/"

  me = signal<User | null>(null)

  getMe() {
    return this.http.get<User>(`${this.baseApiUrl}user`, {headers: this.getHeaders()})
      .pipe(tap(res => this.me.set(res)))
  }

  private getHeaders() {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Service-Name': 'ecofin-front'
    })
  }
}
