import { ProfileService } from './../profile/profile.service';
import { AppService } from './../app.service';
import { LoginService } from './../login/login.service';
import { HomeService } from './../home/home.service';
import { Component, OnInit, Pipe } from '@angular/core';
import { Tweet, User } from '../model/DataModel';
import { Observable } from 'rxjs/internal/Observable';
import { Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';


export interface server{
  instanceType: string;
  name: string;
  status: string;
  started: Date;

}

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  user: User;
  users: User[] = [];
  tweets: Tweet[] = [];
  searchedTweets: Tweet[] = [];
  searchedUsers: User[] = [];
  username: string;
  filteredText = '';
  showSearchedResults: boolean = false;
  status: boolean = false;
 

  constructor(
    private homeService: HomeService,
    private loginService: LoginService,
    private appService: AppService,
    private profileService: ProfileService
  ) { }

  ngOnInit(): void {
    this.getUsers();
    this.getTweets();
  }

  getUsers(): void{
    this.homeService.getUsers().subscribe(
      (res: User[]) => {
        this.users = res;
        this.getUsername();
        this.username = this.loginService.getSignedinUser();
        this.users.forEach( user => {
          if (user.profilePicture !== undefined){
            user.profilePicture = 'data:image/jpeg;base64,'+user.profilePicture;
          }else {
            user.profilePicture = "/assets/img/profile_pic.jpg";
          }


          if (user.username === this.username){
            this.user = user;
            this.appService.userId.emit(user.id);
            this.status =true;
          }
        });
      }
    );
  }

  getUsername(): void{
    this.username = this.loginService.getSignedinUser();
  }

  getTweets(): void{
    this.homeService.getTweets().subscribe(
      (res: Tweet[]) => {
        this.tweets = res;
        this.tweets.forEach( tweet => {
          tweet.tweetImage = 'data:image/jpeg;base64,'+tweet.tweetImage;
        });
      }
    );
  }

  viewResults(searchText: string): void{

    this.tweets.forEach( tweet => {
      if (tweet.tweetBody.toLocaleLowerCase().includes(searchText.toLocaleLowerCase())){
        this.searchedTweets.push(tweet);
      }
    });
    
    this.users.forEach( user => {
      if ( user.firstName.toLocaleLowerCase().includes(searchText.toLocaleLowerCase()) ||
           user.lastName.toLocaleLowerCase().includes(searchText.toLocaleLowerCase())){
             this.searchedUsers.push(user);
           }
    });

    this.showSearchedResults = true;

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
    
    this.profileService.likeTweet(tweetId).subscribe(
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

  onDeleteTweet(tweetId: number): void{
    this.profileService.deleteTweet(tweetId).subscribe(
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

  onFollowUser(usernameOfUserToFollow: string): void{

    this.profileService.follow(usernameOfUserToFollow).subscribe(
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

  onAddTweetToList(tweetId): void{
    this.homeService.addTweetToList(tweetId).subscribe(
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

  onHideTweet(tweetId): void{
    this.homeService.hideTweet(tweetId).subscribe(
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
  
  onMute(usernameToMute: string): void{
    this.homeService.mute(usernameToMute).subscribe(
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
  
  onBlock(usernameToBlock: string): void{
    this.homeService.block(usernameToBlock).subscribe(
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

  onBookmark(tweetId): void{
    this.homeService.bookmark(tweetId).subscribe(
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

}
