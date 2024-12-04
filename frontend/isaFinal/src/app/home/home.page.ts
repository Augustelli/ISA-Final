import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { catchError, tap, of } from 'rxjs';
import { ContadorService } from 'src/service/contador.service';
import { HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  imports: [CommonModule, FormsModule, IonicModule, HttpClientModule],
  providers: [ContadorService]
})
export class HomePage {
  private _contadorService = inject(ContadorService)
  private _router = inject(Router)

  counter: number = 0;
  customValue: number = 42069;
  isOnline: boolean = navigator.onLine

  constructor() {
    window.addEventListener('online', () => this.syncPendingRequests());
    window.addEventListener('offline', () => this.isOnline = false);
  }

  modifyCounter(value: number): void {
    this.counter += value;
    localStorage.setItem('counter', this.counter.toString());
    if (this.isOnline) {
      this.syncPendingRequests();
    }
  }




  ngOnInit(): void {
    this.loadCounter();
    this.syncPendingRequests();
  }

  loadCounter() {
    const storedCounter = localStorage.getItem('counter');
    this._contadorService.getCounterValue().pipe(
      tap(value => {
        console.log("Valor obtenido para persistir contador", value);
        this.counter = value !== undefined ? value : 0;
        localStorage.setItem('counter', this.counter.toString());
      }),
      catchError(error => {
        console.error('Error fetching counter value:', error);
        this.counter = 0;
        return of(0);
      })
    ).subscribe();

  }


  logOut(): void {
    localStorage.removeItem("jwt")
    localStorage.removeItem("counter")
    this._router.navigate(['/login'])
  }

  syncPendingRequests() {
    console.log("ONLINE", this.isOnline);
    
    if (navigator.onLine) {
      this.isOnline = true;
      this._contadorService.addValueToCounter(0).pipe(
        tap(result => {
          console.log("SYNC addValueToCounter 0 ", result)
          if (result == 200) {
            this._contadorService.addValueToCounter(this.counter).pipe(
              tap(addResult => {
                console.log(`Counter synced with value ${this.counter}`);
                localStorage.removeItem('counter');
              }),
              catchError(error => {
                console.error('Error syncing counter:', error);
                return of(null);
              })
            ).subscribe();
          }
        }),
        catchError(error => {
          console.error('Error resetting counter:', error);
          return of(null);
        })
      ).subscribe();
    }
  }

  resetCounter() {
    this.counter = 0;
    localStorage.setItem('counter', this.counter.toString());
    if (this.isOnline) {
      this.syncPendingRequests();
    }
  }

}


