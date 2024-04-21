
import { LoginService } from './../login/login.service';
import { AppService } from './../app.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from "@angular/core";
import { ProfileService } from './profile.service';
import { HttpErrorResponse } from '@angular/common/http';
import { User, Tweet } from '../model/DataModel';
import { HomeService } from '../home/home.service';
import { Observable, Subscription } from 'rxjs';


// @HostListener('window:scroll',)
@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['/profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy{


    userId: number;
    user: User;
    currentUser: User;
    username: string;
    users: User[];
    tweets: Tweet[];
    pinnedTweet: Tweet;
    numFollowing: number;
    paramSubscription: Subscription;
    hasPinnedTweet: boolean = false;
    showFeed: boolean = false;
    hasTweets: boolean = false;
    isUserFollowed: boolean = false;
    showBioDetails: boolean = false;
    status: boolean = false;
    constructor(
      private profileService: ProfileService, 
      private loginService: LoginService,
      private appService: AppService,
      private homeService: HomeService,
      private route: ActivatedRoute,
      private router: Router){}

    ngOnInit(): void {
        this.userId = Number(this.route.snapshot.paramMap.get("userId"));
        this.getUser();
        this.getUsers();
        this.getTweets();
        this.paramSubscription = this.getRouteParamObservable.subscribe();
    }

    ngOnDestroy(): void{
      this.paramSubscription.unsubscribe();
    }

    getRouteParamObservable = Observable.create(observer => {
        setInterval(() => {
          let newUserId: number = Number(this.route.snapshot.paramMap.get("userId"));
          if (this.userId !== newUserId){
            observer.next(
              this.userId = Number(this.route.snapshot.paramMap.get("userId")),
              this.getUser(),
              this.getUsers(),
              this.getTweets(),
            );
          }
        }, 1);
    });

    getTweets(): void{
        this.profileService.getTweets().subscribe(
          (response: Tweet[]) => {
            if (response.length > 0){
              this.hasTweets = true;
            }
            this.tweets = response;
            this.tweets.forEach( (tweet) => {
              if (tweet.tweetImage !== undefined){
                tweet.tweetImage = 'data:image/jpeg;base64,'+tweet.tweetImage;
              }
              tweet.comments.reverse();
              tweet.comments.forEach( comment => {
                if(comment.commentImage !== undefined){
                  comment.commentImage = 'data:image/jpeg;base64,'+comment.commentImage;
                }
                comment.commentReplyList.reverse();
                comment.commentReplyList.forEach( commentReply => {
                  if(commentReply.commentReplyImage !== undefined){
                    commentReply.commentReplyImage ='data:image/jpeg;base64,'+commentReply.commentReplyImage;
                  }
                });
              });
            });
            this.getPinnedTweet();
          },
          (error: HttpErrorResponse) => {
            console.log(error.message);
          }
        );
    }

    getPinnedTweet(): void{
      if (this.currentUser.pinnedTweet !== undefined){
        this.tweets.forEach( tweet =>{
          if (this.currentUser.pinnedTweet.tweetId === tweet.id){
            this.pinnedTweet = tweet;
            this.hasPinnedTweet = false;
          }
        });
      }
    }
      
    getUsers(): void{
        this.profileService.getUsers().subscribe(
          (response: User[]) => {
            this.users = response;
            this.users.forEach((user)=>{
              if (user.profilePicture !== undefined){
                user.profilePicture = 'data:image/jpeg;base64,'+user.profilePicture;
              }else {
                user.profilePicture = "/assets/img/profile_pic.jpg";
              }


              if (user.id === this.userId){
                this.user = user;
                this.status = true;
              }
              if (this.username === user.username){
                this.appService.userId.emit(user.id);
                this.currentUser = user;
                this.numFollowing = user.followList.length;
                this.showBioDetails = true;
              }
            });
            this.checkIfIsUserFollowed();
            this.showFeed = true;
          },
          (error: HttpErrorResponse) => {
            console.log(error.message);
          }
        );
    }

    getUser(): void{
      this.username = this.loginService.getSignedinUser();
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

    onLikeComment(commentId: number, tweetId: number): void{

      this.tweets.forEach( tweet =>{
        if (tweet.id === tweetId){
          tweet.comments.forEach( (comment) => {
            if (comment.id === commentId){
              if (comment.isUserLiked === true){
                comment.isUserLiked = !comment.isUserLiked;
                comment.commentLikes = comment.commentLikes - 1;
              }else {
                comment.isUserLiked = !comment.isUserLiked;
                comment.commentLikes = comment.commentLikes + 1
              }
            }
            
          });
        }
      });
      
      this.profileService.likeComment(commentId).subscribe(
          (response: string) => {
              console.log(response);
              this.getUsers();
              this.getTweets()
          },
          (error: HttpErrorResponse) => {
            console.log(error.message);
            this.getUsers();
            this.getTweets();
          }
      );
    }

    onDeleteComment(commentId: number, tweetId): void{
      const data = new FormData();
      data.append('tweetId', String(tweetId));
      data.append('commentId', String(commentId));
      this.profileService.deleteComment(data).subscribe(
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

    onLikeCommentReply(tweetId: number, commentId: number, commentReplyId: number): void{

      this.tweets.forEach( tweet => {
        if (tweet.id === tweetId){
          tweet.comments.forEach( comment => {
            if (comment.id === commentId){
              comment.commentReplyList.forEach( commentReply => {
                if (commentReply.id === commentReplyId){
                  if ( commentReply.isUserLiked === true){
                      commentReply.isUserLiked = !commentReply.isUserLiked;
                      commentReply.commentReplyLikes = commentReply.commentReplyLikes - 1;
                  }else{
                      commentReply.isUserLiked = !commentReply.isUserLiked;
                      commentReply.commentReplyLikes = commentReply.commentReplyLikes + 1;
                  }
              }
              });
            }
          });
        }
      });

      this.profileService.likeCommentReply(commentReplyId).subscribe(
          (response: string) => {
              console.log(response);
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

    onDeleteCommentReply(commentId: number, commentReplyId: number): void{

        const data = new FormData();
        data.append('commentId', String(commentId));
        data.append('commentReplyId', String(commentReplyId));
        this.profileService.deleteCommentReply(data).subscribe(
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
          this.getUser();
          this.getUsers();
          this.checkIfIsUserFollowed();
        },
        (error: HttpErrorResponse) => {
            console.log(error.message);
            this.getUser();
            this.getUsers();
            this.checkIfIsUserFollowed();
        }
      );
    }

    checkIfIsUserFollowed(): void{
      this.currentUser.followList.forEach( follow => {
        if ( follow.username === this.user.username ){
          console.log("follow.username === this.username");
          this.isUserFollowed = true;
        }else{
          this.isUserFollowed = false;
        }
      });
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