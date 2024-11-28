import { Component, inject, Injectable } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError, Observable, tap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UsuarioService } from 'src/service/usuario.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { AlertController, IonicModule } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule, HttpClientModule],
  providers : [UsuarioService]
})
export class LoginComponent {

  private apiUrl = environment.url;
  private userService = inject(UsuarioService);
  private router = inject(Router);
  private alertController = inject(AlertController)

  credentials = {
    username: '',
    password: '',
    rememberMe: false
  };
  constructor() { }

  async mostrarAlerta(message: string) {
    const alert = await this.alertController.create(
      {
        header : 'Error',
        message: message,
        buttons: ['Ok']
      }
    )
    await alert.present();
  }

  authUser() {
    console.log("CREDENCIALTAS", this.credentials);
    
    this.userService.login(this.credentials).pipe(
      tap(response => {
        console.log("RESPUESTA SERVICIO AUTH", response, response.body);
        
        if (response.id_token != null) {
          console.log("Autenticación exitosa");
          const token = response.id_token;
          localStorage.setItem('jwt', token);
          this.router.navigate(['/home']);
        }
      }),
      catchError(error => {
        console.error('Login failed', error);
        return of(null);
      })
    ).subscribe();
  }
}