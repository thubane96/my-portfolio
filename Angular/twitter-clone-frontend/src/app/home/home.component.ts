import { AppService } from './../app.service';
import { HomeService } from './home.service';
import { Notifications, Tweet, TweetData, User } from './../model/DataModel';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { WidgetService } from '../widget/widget.service';
import { LoginService } from '../login/login.service';
import { NotificationService } from '../notification/notification.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  selectedFile: File;
  tweets: Tweet[];
  username: string;
  users: User[];
  user: User;
  hasTweets: boolean = false;
  status: boolean = false;
  numberOfNotifications: number = 0;
  numberOfUnopenedMessages: number = 0;

  constructor(
    private homeService: HomeService, 
    private loginService: LoginService, 
    private appService: AppService,
    private notificationService: NotificationService,
    private router: Router) { }

  ngOnInit(): void {
    this.signOut();
    this.getTweets();
    this.getUsers();
    this.getUser();
    //this.getNotifications();
  }

  onViewComments(tweetId: number): void{
    this.router.navigate(['comment',tweetId],);
  }

  public onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

  onTweet(tweet: NgForm): void {
    
    let tweetBody: {tweetBody: string} = tweet.value;

    const tweetData = new FormData();
    if(tweetBody.tweetBody !== undefined){
      tweetData.append('tweetBody', tweetBody.tweetBody);
    }
    if(this.selectedFile !== undefined){
      tweetData.append('tweetImage', this.selectedFile);
    }

    if(tweetBody.tweetBody !== undefined || this.selectedFile !== undefined){
      this.homeService.saveTweet(tweetData).subscribe(
        (response: string) => {
          tweet.reset();
          this.getTweets();
          this.getUsers();
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          tweet.reset();
          this.getTweets();
          this.getUsers();
        }
      );
    }
  }

  getTweets(): void{
    this.homeService.getTweets().subscribe(
      (response: Tweet[]) => {
        if (response.length > 0){
          this.hasTweets = true;
        }
        this.tweets = response;
        this.tweets.forEach( (tweet) => {
          if(tweet.tweetImage !== undefined){
            tweet.tweetImage = 'data:image/jpeg;base64,'+tweet.tweetImage;
          }
        });
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    );
  }


  onLikeTweet(tweetId: number): void{

    this.tweets.forEach( (tweet) => {

      if (tweet.id === tweetId){

        if (tweet.isUserLiked === true){
		      tweet.isUserLiked = !tweet.isUserLiked;
          tweet.likes = tweet.likes - 1;
        }else {
	        tweet.isUserLiked = !tweet.isUserLiked;
          tweet.likes = tweet.likes + 1;
        }
      }
    });
    
    this.homeService.likeTweet(tweetId).subscribe(
      (response: string) => {
        console.log(response);
        this.getTweets();
        this.getUsers();
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        this.getTweets();
        this.getUsers();
      }
    );
  }

  getUser(): void {
    this.username = this.loginService.getSignedinUser();
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


          if (user.username === this.username){
            this.appService.userId.emit(user.id);
            this.user = user;
            this.status = true;
          }
        });
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    );
  }

  onDeleteTweet(tweetId: number): void{
    this.homeService.deleteTweet(tweetId).subscribe(
      (res: string) => {
        console.log(res);
        this.getUsers();
        this.getTweets();
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        this.getUsers();
        this.getTweets();
      }
    );
  }


  onPinTweet(tweetId: number): void{
    this.homeService.pinTweet(tweetId).subscribe(
      (res: string) => {
        console.log(res);
        this.getUser();
        this.getUsers();
        this.getTweets();
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        this.getUser();
        this.getUsers();
        this.getTweets();
      }
    );
  }

  onAddTweetToList(tweetId): void{
    this.homeService.addTweetToList(tweetId).subscribe(
      (res: string) => {
        console.log(res);
        this.getUser();
        this.getUsers();
        this.getTweets();
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        this.getUser();
        this.getUsers();
        this.getTweets();
      }
    );
  }

  onHideTweet(tweetId): void{
    this.homeService.hideTweet(tweetId).subscribe(
      (res: string) => {
        console.log(res);
        this.getUser();
        this.getUsers();
        this.getTweets();
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        this.getUser();
        this.getUsers();
        this.getTweets();
      }
    );
  }

  onFollowUser(usernameOfUserToFollow: string): void{

    this.homeService.follow(usernameOfUserToFollow).subscribe(
      (res: string) => {
        console.log(res);
        this.getUser();
        this.getUsers();
        this.getTweets();
      },
      (error: HttpErrorResponse) => {
          console.log(error.message);
          this.getUser();
          this.getUsers();
          this.getTweets();
      }
    );
  }

  onMute(usernameToMute: string): void{
    this.homeService.mute(usernameToMute).subscribe(
      (res: string) => {
        console.log(res);
        this.getUser();
        this.getUsers();
        this.getTweets();
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        this.getUser();
        this.getUsers();
        this.getTweets();
      }
    );
  }

  onBlock(usernameToBlock: string): void{
    this.homeService.block(usernameToBlock).subscribe(
      (res: string) => {
        console.log(res);
        this.getUser();
        this.getUsers();
        this.getTweets();
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        this.getUser();
        this.getUsers();
        this.getTweets();
      }
    );
  }

  onBookmark(tweetId): void{
    this.homeService.bookmark(tweetId).subscribe(
      (res: string) => {
        console.log(res);
        this.getUser();
        this.getUsers();
        this.getTweets();
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        this.getUser();
        this.getUsers();
        this.getTweets();
      }
    );
  }

  getNotifications(): void{
    this.notificationService.getNotifications().subscribe(
      (res: Notifications[]) => {
        res.forEach( notification => {
          if (notification.username === this.user.username && notification.opened === false){
            this.numberOfNotifications = this.numberOfNotifications + 1;
          }
        });
        this.status = true;
        this.appService.numberOfNotifications.emit(this.numberOfNotifications);
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    );
  }

  signOut(): void {
    if (!this.loginService.isUserSignedin){
      this.loginService.signout();
      location.reload();
    }
    
  }
  

}
