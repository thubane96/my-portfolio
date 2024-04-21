import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from './../login/login.service';
import { HomeService } from './../home/home.service';
import { Component, OnInit } from '@angular/core';
import { Notifications, User } from '../model/DataModel';
import { HttpErrorResponse } from '@angular/common/http';
import { NotificationService } from './notification.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {

  users: User[];
  user: User;
  notificationList: Notifications[] = [];
  username: string;
  status: boolean = false;

  constructor(
    private notificationService: NotificationService,
    private homeService: HomeService,
    private loginService: LoginService,
    private route: Router
  ) { }

  ngOnInit(): void {
    this.getUsername();
    this.getUsers();
    this.getNotifications();
  }

  getUsers(): void{
    this.getUsername();
    this.homeService.getUsers().subscribe(
      (res: User[]) => {
        this.users = res;
        this.users.forEach( user => {
          if (user.profilePicture !== undefined){
            user.profilePicture = 'data:image/jpeg;base64,'+user.profilePicture;
          }else {
            user.profilePicture = "/assets/img/profile_pic.jpg";
          }


          if (user.username === this.username){
            this.user = user
          }
        });
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    );
  }

  getUsername(): void{
    this.username = this.loginService.getSignedinUser();
  }

  getNotifications(): void{
    this.notificationService.getNotifications().subscribe(
      (res: Notifications[]) => {
        res.forEach( notification => {
          if (notification.username === this.loginService.getSignedinUser() && notification.opened === false){
            this.notificationList.push(notification);
          }
        });
        this.status = true;
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    );
  }

  onClickNotification(userId: number, notification: Notifications): void{
    this.notificationService.openNotification(notification.id).subscribe(
      (res: string) => {
        console.log(res);

        if (notification.forUser === true){
          this.route.navigate(["/profile", userId]);
        }

        if (notification.forTweet === true){
          this.route.navigate(["/comment", notification.tweetId]);
        }

        if (notification.forComment === true || notification.forCommentReply === true){
          this.route.navigate(["/commentReply", notification.commentId, notification.tweetId]);
        }
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        if (notification.forUser === true){
          this.route.navigate(["/profile", userId]);
        }

        if (notification.forTweet === true){
          this.route.navigate(["/comment", notification.tweetId]);
        }

        if (notification.forComment === true || notification.forCommentReply === true){
          this.route.navigate(["/commentReply", notification.commentId, notification.tweetId]);
        }
      }
    );
  }

}
