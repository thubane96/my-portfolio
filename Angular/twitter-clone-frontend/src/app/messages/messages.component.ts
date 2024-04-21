import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from './../login/login.service';
import { HomeService } from './../home/home.service';
import { AppService } from './../app.service';
import { Component, OnInit } from '@angular/core';
import { Message, User } from '../model/DataModel';
import { MessagesService } from './messages.service';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.scss']
})
export class MessagesComponent implements OnInit {

  user: User;
  users: User[];
  username: string;
  messages: Message[] = [];
  filteredText = '';
  status: boolean = false;

  constructor(
    private appService: AppService,
    private homeService: HomeService,
    private loginService: LoginService,
    private messagesService: MessagesService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getUsername();
    this.getUsers();
  }

  getUsers(): void{
    this.homeService.getUsers().subscribe(
      (res: User[]) => {
        res.forEach( user => {
          if (user.profilePicture !== undefined){
            user.profilePicture = 'data:image/jpeg;base64,'+user.profilePicture;
          }

          if(user.username === this.username){
            this.user = user;
            this.appService.userId.emit(user.id);
          }
        });
        this.users = res;
        this.status = true;
      }
    );
  } 

  onClickMessage(userId: number, usernameOfFriendMessaging): void{
    this.messagesService.openMessage(usernameOfFriendMessaging).subscribe(
      (res: string) => {
        console.log(res);
        this.router.navigate(['/message', userId, "messages"]);
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        this.router.navigate(['/message', userId, "messages"]);
      }
    );

  }

  getUsername(): void{
    this.username = this.loginService.getSignedinUser();
  }

  
}
