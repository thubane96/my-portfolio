import { AppService } from './app.service';
import { HomeService } from './home/home.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { LoginService } from './login/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  isAuthorized: boolean = false;
  IsTokenValid: boolean;
  opened: boolean = true;
  checkIfAuthorized: boolean;
  userId: number;
  numberOfNotifications: number = 0;
  numberOfUnopenedMessages: number = 0;

  constructor(
    private loginService: LoginService, 
    private appService: AppService,
    private homeService: HomeService){}

  ngOnInit(): void {
    this.checkIfIsAuthorized();
    let isTokenEmpty: boolean = this.loginService.isTokenEmpty();
    console.log("isTokenEmpty: "+ isTokenEmpty);
    let isUserSignedin: boolean = this.loginService.isUserSignedin();
    console.log("isUserSignedin: "+ isUserSignedin);
    if(isUserSignedin === false){
     this.loginService.signout();
    }
    this.appService.userId.subscribe(
      (res: number) => {
        this.userId = res;
      }
    );

    this.appService.numberOfNotifications.subscribe(
      (res: number) => {
        this.numberOfNotifications = res;
      }
    );

    this.appService.numberOfUnopenedMessages.subscribe(
      (res: number) => {
        this.numberOfUnopenedMessages = res;
      }
    );
  }

  onCheckAuth(auth: any){
    this.isAuthorized = auth;
  }

  checkIfIsAuthorized(): void{
    this.isAuthorized = this.loginService.isUserSignedin();
  }s

  onSignout(): void{
    this.loginService.signout();
    this.isAuthorized = false;
  }
  

}
