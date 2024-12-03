import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { UsuarioService } from 'src/service/usuario.service';
import { catchError, of, tap } from 'rxjs';
import { HttpClient, HttpClientModule } from '@angular/common/http';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule, HttpClientModule],
  providers : [UsuarioService]
})
export class RegisterComponent implements OnInit {

  private _router = inject(Router)
  private _userService = inject(UsuarioService)

  errorMessage: string | null = null;

  constructor() { }


  register = {
    login: "",
    email: "",
    password: "",
    langKey: "es"
  }

  registerUser() {
    this._userService.registerUser(this.register).pipe(
      tap(response => {
        console.log("RESPONSE SERVICE");
        
        if (response.status === 201) {
          window.alert("Usuario regsitrado con éxito")
          this._router.navigate(['/login']);
        }
      }),
      catchError(error => {
        console.error("Error al registrar usuario", error);
        this.errorMessage = 'Error al registrar usuario. Por favor, inténtelo de nuevo.';
        return of(null);
      })
    ).subscribe();
  }

  ngOnInit() { }

}
