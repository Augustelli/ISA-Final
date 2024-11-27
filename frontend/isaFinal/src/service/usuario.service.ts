import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/api/authenticate';
  private _httpClient = inject(HttpClient)
  
  private headers_http = new HttpHeaders({
    'Authorization': 'Bearer '
  });

  constructor() {}

  login(credentials: { username: string; password: string; rememberMe: any }): Observable<any> {
    console.log("Credeciales en servicio: ", credentials);
    
    return this._httpClient.post<any>(
      this.apiUrl,
      credentials,
      //{ headers: this.headers_http }
    );
  }
}