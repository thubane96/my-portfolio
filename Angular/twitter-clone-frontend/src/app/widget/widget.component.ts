import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { post } from 'jquery';
import { interval, Observable } from 'rxjs';
import { HomeService } from '../home/home.service';
import { LoginService } from '../login/login.service';
import { Tweet, User } from '../model/DataModel';
import { WidgetService } from './widget.service';

@Component({
  selector: 'app-widget',
  templateUrl: './widget.component.html',
  styleUrls: ['./widget.component.scss']
})
export class WidgetComponent implements OnInit, OnDestroy {

  tweet: Tweet;
  tweets: Tweet[];
  users: User[];
  username: string;
  postId: number;
  min: number = 1;
  max: number;
  getRandomTweetSubscription: any;
  filteredText = '';

  


  constructor(
    private widgetService: WidgetService,
    private loginService: LoginService,
    private homeService: HomeService,
    private router: Router) { 
    
  }

  ngOnInit(): void {
    this.getUsers();
    this.getTweet();
    this.getUser();
    this.getRandomTweetSubscription = this.getRandomTweetObservable.subscribe();

  }

  ngOnDestroy(): void{
    this.getRandomTweetSubscription.unsubscribe();
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

    this.widgetService.likeTweet(tweetId).subscribe(
      (response: string) => {
        console.log(response);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  getRandomTweetObservable = Observable.create(observer => {
    setInterval(() => {
      if (this.loginService.isUserSignedin() === true)  observer.next(this.getUsers(), this.getTweet());}, 60000);
  });

  getTweet(): void{
    this.widgetService.getTweets().subscribe(
      (response: Tweet[]) => {
        this.tweets =response;
        this.max = this.tweets.length;
        this.postId = Math.floor(Math.random() * (this.max - this.min + 1) + this.min);
        this.tweets.forEach((tweet) =>{
          if (tweet.id === this.postId){
            if(tweet.tweetHasImage === true){
              tweet.tweetImage = 'data:image/jpeg;base64,'+tweet.tweetImage;
            }
            this.tweet = tweet;
            return;
          }
        }); 

        this.tweets.forEach( tweet => {
          if(tweet.tweetHasImage === true && tweet.id !== this.postId){
            tweet.tweetImage = 'data:image/jpeg;base64,'+tweet.tweetImage;
          }
        });
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    );
  }

  getUsers(): void{
     this.widgetService.getUsers().subscribe(
       (response: User[]) => {
         this.users = response;
         this.users.forEach( user => {
           if (user.profilePicture !== undefined){
            user.profilePicture = 'data:image/jpeg;base64,'+user.profilePicture;
           }else {
            user.profilePicture = "/assets/img/profile_pic.jpg";
          }
         });
       }
     );
  }

  getUser(): void{
    this.username = this.loginService.getSignedinUser();
  }

  onBookmark(tweetId): void{
    this.homeService.bookmark(tweetId).subscribe(
      (res: string) => {
        console.log(res);
        this.getUsers();
        this.getTweet();
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        this.getUsers();
        this.getTweet();
      }
    );
  }

}
