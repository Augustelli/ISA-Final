import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule],


})
export class LoginComponent  implements OnInit {

  constructor(private router: Router) {}

  ngOnInit() {}

  login() {
    // Lógica de autenticación
    this.router.navigate(['/home']);
  }
}

