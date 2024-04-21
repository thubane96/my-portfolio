import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  signupError: boolean = false;
  signupSuccessful: boolean = false;
  signinError: boolean = false;
  progressBar: boolean = false;
  isChanged: string = 'home';

  @Output('atCheckAuth') isAuthoriziedEvent = new EventEmitter<boolean>();


  constructor(private loginService: LoginService){}

  onCheckAuthority(){
    this.isAuthoriziedEvent.emit(this.loginService.isUserSignedin());9
  }

  onSignup(signupForm: NgForm): void {
    this.loginService.signup(signupForm.value).subscribe(
      (response: string) => {
        if (response === "false") {
          this.signupError = true;
        } else {
          this.signupSuccessful = true;
          setTimeout(() => {
            this.isChanged = 'login';
          }, 3000);
          
        }
        signupForm.reset();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
        signupForm.reset();
      }
    );
  }
  
  getChecked(): void {
    this.loginService.getByUserRole().subscribe(
      (response: String) => {
        
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  onLogin(loginForm: NgForm): void {
   this.progressBar = true;
   this.loginService.signin(loginForm.value).subscribe(
      (response: any) => {
        this.onCheckAuthority();
        loginForm.reset();
        this.isChanged = 'login'
        this.progressBar = false;
        console.log("Token is here: "+ response.token);
        // location.reload();
        // this.getChecked();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
        loginForm.reset();
        this.progressBar = false;
      }
    );
  }


  onChange(choice: string): void {
    if (choice === 'home') {
      this.isChanged = 'home'
    }
    if (choice === 'login') {
      this.isChanged = 'login';
    }
    if (choice === 'signup') {
      this.isChanged = 'signup';
    }
    
  }
}
