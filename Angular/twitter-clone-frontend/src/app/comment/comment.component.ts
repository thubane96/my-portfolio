import { AppService } from './../app.service';
import { NgForm } from '@angular/forms';
import { Tweet, User } from '../model/DataModel';
import { Component, OnInit } from "@angular/core";
import { CommentService } from "./comment.service";
import { HttpErrorResponse } from '@angular/common/http';
import { HomeService } from '../home/home.service';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../login/login.service';

@Component({
    selector: 'app-comment',
    templateUrl: './comment.component.html',
    styleUrls: ['./comment.component.scss']
})

export class CommentComponent implements OnInit{

    tweet: Tweet;
    showTweet: boolean = false;
    private tweetId: number;
    username: string;
    users: User[];
    selectedFile: File;
    status: boolean = false;

    constructor(
      private commentService: CommentService,
      private loginService: LoginService,
      private homeService: HomeService,
      private appService: AppService,
      private router: Router,
      private route: ActivatedRoute){}

    ngOnInit(): void {
      this.tweetId = Number(this.route.snapshot.paramMap.get("tweetId"));
      this.getUser();
      this.getUsers();
      this.onGetTweet(this.tweetId);
    }

    onGetTweet(tweetId: number): void{
      this.commentService.getTweet(tweetId).subscribe(
        (res: Tweet) => {
          this.tweet = res;
          if(this.tweet.tweetImage !== undefined){
            this.tweet.tweetImage = 'data:image/jpeg;base64,'+this.tweet.tweetImage;
          }
          this.tweet.comments.reverse();
          this.tweet.comments.forEach( comment => {
            if (comment.commentImage !== undefined){
              comment.commentImage = 'data:image/jpeg;base64,'+comment.commentImage;
            }
          });
          this.showTweet = true;
          this.status = true;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
    }

    public onFileChanged(event) {
      this.selectedFile = event.target.files[0];
    }

    onAddComment(formData: NgForm): void{

      let comment: { tweetId: string, commentBody: string } = formData.value;
      const commentData = new FormData();
      commentData.append('tweetId', comment.tweetId);

      if (comment.commentBody !== undefined) {
        commentData.append('commentBody', comment.commentBody);
      }
      if (this.selectedFile !== undefined) {
        commentData.append('commentImage', this.selectedFile);
      }

      if (comment.commentBody !== undefined || this.selectedFile !== undefined){
        this.commentService.addComment(commentData).subscribe(
          (response: string) => {
              console.log(response);
              formData.reset();
              this.getUsers();
              this.onGetTweet(this.tweetId);
          },
          (error: HttpErrorResponse) => {
            console.log(error.message);
            formData.reset();
            this.getUsers();
            this.onGetTweet(this.tweetId);
          }
      );
      }
    }

    onLikeTweet(tweetId: number,): void{

      if (this.tweet.isUserLiked === true){
        this.tweet.isUserLiked != this.tweet.isUserLiked;
        this.tweet.likes = this.tweet.likes - 1;
      }else {
        this.tweet.isUserLiked != this.tweet.isUserLiked;
        this.tweet.likes = this.tweet.likes + 1;
      }
      
      this.commentService.likeTweet(tweetId).subscribe(
        (response: string) => {
          console.log(response);
          this.getUsers();
          this.onGetTweet(this.tweetId);
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          this.getUsers();
          this.onGetTweet(this.tweetId);
        }
      );
    }

    onLikeComment(commentId: number): void{
      
      this.tweet.comments.forEach( (comment) => {
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
      this.commentService.likeComment(commentId).subscribe(
          (response: string) => {
              console.log(response);
              this.getUsers();
              this.onGetTweet(this.tweetId);
          },
          (error: HttpErrorResponse) => {
            console.log(error.message);
            this.getUsers();
            this.onGetTweet(this.tweetId);
          }
      );
    }

    getUser(): void{
      this.username = this.loginService.getSignedinUser();
    }
    
    getUsers(): void{
      this.commentService.getUsers().subscribe(
        (response: User[]) => {
          this.users = response;
          this.users.forEach( user => {
            if(user.profilePicture !== undefined){
              user.profilePicture = 'data:image/jpeg;base64,'+user.profilePicture;
            }else {
              user.profilePicture = "/assets/img/profile_pic.jpg";
            }


            if (this.username  === user.username) {
              this.appService.userId.emit(user.id);
            }
          });
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          this.getUsers();
          this.onGetTweet(this.tweetId);
        }
      );
    }

    onDeleteTweet(): void{
      this.homeService.deleteTweet(this.tweetId).subscribe(
        (res: string) => {
          console.log(res);
          this.router.navigate(['/']);
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          this.router.navigate(['/']);
        }
      );
    }

    onDeleteComment(commentId: number): void{
      const data = new FormData();
      data.append('tweetId', String(this.tweetId));
      data.append('commentId', String(commentId));
      this.commentService.deleteComment(data).subscribe(
        (res: string) => {
          console.log(res);
          this.getUsers();
          this.onGetTweet(this.tweetId);
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          this.getUsers();
          this.onGetTweet(this.tweetId);
        }
      );
    }

    onPinTweet(tweetId: number): void{
      this.homeService.pinTweet(tweetId).subscribe(
        (res: string) => {
          console.log(res);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        }
      );
    }
  
    onAddTweetToList(tweetId): void{
      this.homeService.addTweetToList(tweetId).subscribe(
        (res: string) => {
          console.log(res);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        }
      );
    }
  
    onHideTweet(tweetId): void{
      this.homeService.hideTweet(tweetId).subscribe(
        (res: string) => {
          console.log(res);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        }
      );
    }
  
    onFollowUser(usernameOfUserToFollow: string): void{
  
      this.homeService.follow(usernameOfUserToFollow).subscribe(
        (res: string) => {
          console.log(res);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        }
      );
    }
  
    onMute(usernameToMute: string): void{
      this.homeService.mute(usernameToMute).subscribe(
        (res: string) => {
          console.log(res);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        }
      );
    }
  
    onBlock(usernameToBlock: string): void{
      this.homeService.block(usernameToBlock).subscribe(
        (res: string) => {
          console.log(res);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        }
      );
    }

    onBookmark(tweetId): void{
      this.homeService.bookmark(tweetId).subscribe(
        (res: string) => {
          console.log(res);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          this.getUser();
          this.getUsers();
          this.onGetTweet(this.tweetId);
        }
      );
    }
}