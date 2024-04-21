import { Injectable, EventEmitter } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class AppService {

  userId = new EventEmitter<number>();
  numberOfNotifications = new EventEmitter<number>();
  numberOfUnopenedMessages = new EventEmitter<number>();
  
  constructor() { }
}
