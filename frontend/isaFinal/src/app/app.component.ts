import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { IonicModule } from '@ionic/angular';
import { AuthGuard } from './auth.guard';
import { HomePage } from './home/home.page';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

@Component({
    selector: 'app-root',
    templateUrl: 'app.component.html',
    imports: [IonicModule, RouterOutlet, HttpClientModule, HomePage, LoginComponent, RegisterComponent],
    providers : [AuthGuard]
})
export class AppComponent {
  constructor() {}
} 