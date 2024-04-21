import { NgForm } from '@angular/forms';
import { AppService } from './../app.service';
import { LoginService } from './../login/login.service';
import { HomeService } from './../home/home.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { Message, Messages, User } from '../model/DataModel';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService } from './message.service';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent implements OnInit {

  friendUserId: number;
  where: string;
  user: User;
  users: User[];
  username: string;
  currentUser: User;
  messages: Messages[] = [];
  selectedFile: any;
  status: boolean = false;

  constructor(
    private homeService: HomeService,
    private loginService: LoginService,
    private appService: AppService,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.friendUserId = Number(this.route.snapshot.paramMap.get("friendUserId"));
    this.where = this.route.snapshot.paramMap.get("where");
    this.getUser();
    this.getUsers();

  }

  public onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

  getUsers(): void{
    this.homeService.getUsers().subscribe(
      (response: User[]) => {
        this.users = response;
        this.users.forEach((user)=>{
          if (user.profilePicture !== undefined){
            user.profilePicture = 'data:image/jpeg;base64,'+user.profilePicture;
          }else {
            user.profilePicture = "/assets/img/profile_pic.jpg";
          }


          if (user.id === this.friendUserId){
            this.user = user;
            this.status = true;
          }
          if (this.username === user.username){
            this.appService.userId.emit(user.id);
            this.currentUser = user;
          }
        });

        this.currentUser.messageList.forEach( message => {
          if (message.friendMessaging === this.user.username){
            message.messagesList.forEach( message => {
              if (message.messageImage !== undefined){
                message.messageImage = 'data:image/jpeg;base64,'+message.messageImage;
              }
            });
          }
        });
       
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    );
  }

  getUser(): void{
    this.username = this.loginService.getSignedinUser();
  }

  onMessage(message: NgForm, userToUsername: string): void {
    let messageBody: {messageBody: string} = message.value;
    const messageData = new FormData();
    
    messageData.append("userToUsername", userToUsername);
    if ( messageBody.messageBody !== undefined ){
      messageData.append("messageBody", messageBody.messageBody);
    }

    if ( this.selectedFile !== undefined ){
      messageData.append("messageImage", this.selectedFile);
    }

    if ( messageBody.messageBody !== undefined || this.selectedFile !== undefined ){
      
      this.messageService.saveMessage(messageData).subscribe(
        (res: string) => {
          console.log(res);
          message.resetForm();
          this.getUsers();
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          message.resetForm();
          this.getUsers();
        }
      );
    }

  }

  getMessages(): void{
    this.messageService.getMessages().subscribe(
      (res: Message[]) => {
        res.forEach( mssg => {
          if( mssg.friendMessaging === this.user.username){
            mssg.messagesList.reverse();
            mssg.messagesList.forEach( mssg2 => {
              if (mssg2.messageImage !== undefined){
                mssg2.messageImage = 'data:image/jpeg;base64,'+mssg2.messageImage;
              }
              this.messages.push(mssg2);
            });
          }
        });
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    );
  }

}
