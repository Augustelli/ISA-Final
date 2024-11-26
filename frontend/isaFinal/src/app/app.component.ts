import { Component } from '@angular/core';
import { provideRouter, RouterOutlet } from '@angular/router';
import { bootstrapApplication } from '@angular/platform-browser';
import { IonicModule } from '@ionic/angular';
import { AppRoutingModule, routes } from './app.routes';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  standalone: true,
  imports: [IonicModule, RouterOutlet, AppRoutingModule],
})
export class AppComponent {
  constructor() {}
}

bootstrapApplication(AppComponent);