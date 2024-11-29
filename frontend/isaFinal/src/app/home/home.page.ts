import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { catchError, tap, of } from 'rxjs';
import { ContadorService } from 'src/service/contador.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule, HttpClientModule],
  providers: [ContadorService]
})
export class HomePage {
  counter: number = 0;
  customValue: number = 12;



  private _contadorService = inject(ContadorService)
  constructor() { }

  modifyCounter(value: number): void {
    this._contadorService.addValueToCounter(value).pipe(
      tap(result => {
        console.log("RESULTADO DE INCREMENTAR CONTADOR", result);
        if (result && result == 200) {
          this.counter += value;
        }
      }),
      catchError(error => {
        console.error('Error adding value to counter:', error);
        return of(null);
      })
    ).subscribe();
  }

  resetCounter() {
    this.modifyCounter(0)
    this.counter = 0;
  }



  ngOnInit(): void {
    this._contadorService.getCounterValue().pipe(
      tap(value => {
        console.log("Valor obtenido");
        this.counter = value !== undefined ? value : 0;
      }),
      catchError(error => {
        console.error('Error fetching counter value:', error);
        this.counter = 0;
        return of(0);
      })
    ).subscribe();
  }


}


