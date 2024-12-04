import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { IonicModule } from '@ionic/angular';

@Component({
    selector: 'app-root',
    templateUrl: 'app.component.html',
    imports: [IonicModule, RouterOutlet, HttpClientModule]
})
export class AppComponent {
  constructor() {}
}