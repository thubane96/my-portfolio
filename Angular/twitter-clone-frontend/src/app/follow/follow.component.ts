import { ProfileService } from './../profile/profile.service';
import { LoginService } from './../login/login.service';
import { AppService } from './../app.service';
import { ActivatedRoute } from '@angular/router';
import { HomeService } from './../home/home.service';
import { Component, OnInit } from '@angular/core';
import { User } from '../model/DataModel';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-follow',
  templateUrl: './follow.component.html',
  styleUrls: ['./follow.component.scss']
})
export class FollowComponent implements OnInit {

  userId: number;
  user: User;
  users: User[] = [];
  followersUsernames: string[] = [];
  username: string;
  status: boolean = false;

  constructor(
    private homeService: HomeService,
    private appService: AppService,
    private loginService: LoginService,
    private profileService: ProfileService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get("userId"));
    this.getUsers();
  }

  getUsers(): void {
    this.homeService.getUsers().subscribe(
      (res: User[]) => {
        this.users = res;
        this.getUser();
        this.users.forEach( user => {
          if (user.profilePicture !== undefined){
            user.profilePicture = 'data:image/jpeg;base64,'+user.profilePicture;
          }else {
            user.profilePicture = "/assets/img/profile_pic.jpg";
          }


          if (user.id === this.userId){
            this.user = user;
            this.appService.userId.emit(user.id);
          }

          user.followList.forEach( follower => {
            if ( follower.username === this.username){
              this.followersUsernames.push(user.username);
            }
          });
  
        });

        this.status = true;
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    );
  }

  getUser(): void{
    this.username = this.loginService.getSignedinUser();
  }

  onFollowUser(usernameOfUserToFollow: string): void{
    this.profileService.follow(usernameOfUserToFollow).subscribe(
      (res: string) => {
        console.log(res);
        this.getUsers();
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        this.getUsers();
      }
    );
  }

}
