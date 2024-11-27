import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  standalone: true,
  imports: [IonicModule, RouterOutlet],
})
export class AppComponent {
  constructor() {}
}