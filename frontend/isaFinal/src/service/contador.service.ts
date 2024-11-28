import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ContadorService {

  private _httpClient = inject(HttpClient)
  private apiUrl = 'http://localhost:8080/api/contador';


  private headers_http = new HttpHeaders({
    'Authorization': 'Bearer ' + localStorage.getItem("jwt")
  });
  constructor(
  ) { }


  getCounterValue(): Observable<any> {
    console.log("Obteniendo valor de contador");
    return this._httpClient.get<any>(
      this.apiUrl,
      { headers: this.headers_http }
    )
  }
}
