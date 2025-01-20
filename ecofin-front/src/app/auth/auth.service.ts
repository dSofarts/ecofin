import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../environments/environment";
import {catchError, tap, throwError} from "rxjs";
import {LoginResponse, TokenResponse} from "./auth.interface";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  http = inject(HttpClient)
  cookieService = inject(CookieService)
  router = inject(Router)
  baseApiUrl = environment.BASE_BACKEND_URL + "auth/"

  operationId: string | null = null;
  expired: Date | null = null;
  tempToken: string | null = null;
  token: string | null = null;
  refreshToken: string | null = null;

  accountNotConfirmed: boolean | null = null;

  get isLoggedIn(): boolean {
    return !!this.tempToken
  }

  get isAuth(): boolean {
    if (!this.token) {
      this.token = this.cookieService.get("token");
    }
    return !!this.token
  }

  get isConfirmed(): boolean {
    return !!this.accountNotConfirmed
  }

  sendCode(payload: { phone: string, password: string }) {
    const headers = this.getHeaders();
    return this.http.post<LoginResponse>(`${this.baseApiUrl}login`, payload, {headers})
      .pipe(
        tap(res => {
          this.operationId = res.operationId
          this.expired = res.expired
          this.tempToken = res.tempToken
          this.accountNotConfirmed = false
        }),
        catchError(err => {
          if (err.status === 403) {
            this.accountNotConfirmed = true
          }
          return throwError(err)
        })
      )
  }

  login(payload: { otp: string, operationId: string, tempToken: string }) {
    const headers = this.getHeaders();
    return this.http.post<TokenResponse>(`${this.baseApiUrl}authenticate`, payload, {headers})
      .pipe(
        tap(res => {
          this.saveToken(res)
        })
      )
  }

  refreshAccessToken() {
    const headers = this.getHeaders();
    const payload = {
      refreshToken: this.cookieService.get("refreshToken"),
    }
    return this.http.post<TokenResponse>(`${this.baseApiUrl}refresh-token`, payload, {headers})
      .pipe(
        tap(res => {
          this.saveToken(res)
        }),
        catchError(err => {
          this.logout()
          return throwError(err)
        })
      )
  }

  logout() {
    this.cookieService.deleteAll()
    this.token = null;
    this.refreshToken = null;
    this.router.navigate(['/login']);
  }

  private getHeaders() {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Service-Name': 'ecofin-front'
    })
  }

  saveToken(res: TokenResponse) {
    this.token = res.token
    this.refreshToken = res.refreshToken

    this.cookieService.set("token", res.token)
    this.cookieService.set("refreshToken", res.refreshToken)
  }
}
