import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { HomeService } from '../home/home.service';
import { LoginService } from '../login/login.service';
import { Tweet, User } from '../model/DataModel';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  tweets: Tweet[];
  users: User[];
  user: User;
  username: string;
  hasBookmarks: boolean = false;

  constructor(
    private homeService: HomeService,
    private appService: AppService,
    private loginService: LoginService
    ) { }

  ngOnInit(): void {
    this.getUser();
    this.getUsers();
    this.getTweets();
  }

  getTweets(): void{
    this.homeService.getTweets().subscribe(
      (response: Tweet[]) => {
        if (response.length > 0){
          this.hasBookmarks = true;
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
          }
          if (user.username === this.username){
            this.appService.userId.emit(user.id);
            this.user = user;
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

}
