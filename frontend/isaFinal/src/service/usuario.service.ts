import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private apiUrl = 'http://localhost:8080/api';
  private _httpClient = inject(HttpClient)
  
  constructor() {}

  login(credentials: { username: string; password: string; rememberMe: any }): Observable<any> {
    console.log("Credeciales en servicio: ", credentials);
    
    return this._httpClient.post<any>(
      this.apiUrl + "/authenticate",
      credentials
    );
  }


  registerUser(register: { login: string; email: string; password: string; langKey: string; }) : Observable<any> {
    console.log("Registrando usuario", register);
    return this._httpClient.post<any>(
      this.apiUrl + "/register",
      register,
      {observe : 'response'}
    )
    
  }
}