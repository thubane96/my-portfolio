import { AppService } from './../app.service';
import { CommentService } from './../comment/comment.service';
import { NgForm } from '@angular/forms';
import { CommentReplyService } from './comment-reply.service';
import { Component, OnInit } from "@angular/core";
import { Comment, User } from '../model/DataModel';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../login/login.service';
import { HomeService } from '../home/home.service';

@Component({
    selector: 'app-comment-reply',
    templateUrl: './comment-reply.component.html',
    styleUrls: ['./comment-reply.component.scss']
})
export class CommentReplyComponent implements OnInit{

    comment: Comment;
    users: User[];
    selectedFile: File;
    commentId: number;
    tweetId: number;
    username: string;
    showComment: boolean = false;
    status: boolean = false;

    constructor(
        private commentReplyService: CommentReplyService, 
        private loginService: LoginService,
        private commentService: CommentService,
        private appService: AppService,
        private homeService: HomeService,
        private router: Router,
        private route: ActivatedRoute){}

    ngOnInit(): void{
        this.tweetId = Number(this.route.snapshot.paramMap.get("tweetId"));
        this.commentId = Number(this.route.snapshot.paramMap.get("commentId"));
        this.username = this.loginService.getSignedinUser();
        this.getUsers();
        this.getComment(this.commentId);
    }

    getComment(commentId: number): void{
        this.commentReplyService.getComment(commentId).subscribe(
            (response: Comment) => {
                this.comment = response;
                if (this.comment.commentImage !== undefined){
                    this.comment.commentImage = 'data:image/jpeg;base64,'+this.comment.commentImage;
                }
                this.comment.commentReplyList.reverse();
                this.comment.commentReplyList.forEach( commentReply => {
                    if (commentReply.commentReplyImage !== undefined){
                        commentReply.commentReplyImage = 'data:image/jpeg;base64,'+commentReply.commentReplyImage;
                    }
                });
                this.showComment = true;
                this.status = true;
            },
            (error: HttpErrorResponse) => {
              console.log(error.message);
            }
        );
    }

    public onFileChanged(event) {
        this.selectedFile = event.target.files[0];
    }

    getUsers():void{
        this.commentReplyService.getUsers().subscribe(
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
            }
        );
    }

    onAddCommentReply(reply: NgForm): void{
        let commentReplyBody: {commentReplyBody: string} = reply.value;
        const replyData = new FormData();

        replyData.append('commentId', String(this.commentId));

        if (commentReplyBody.commentReplyBody !== undefined){
            replyData.append('commentReplyBody', commentReplyBody.commentReplyBody);
        }
        if(this.selectedFile !== undefined){
            replyData.append('commentReplyImage', this.selectedFile);
        }

        if(commentReplyBody.commentReplyBody !== undefined || this.selectedFile !== undefined){
            this.commentReplyService.addCommentReply(replyData).subscribe(
                (response: string) => {
                    console.log(response);
                    this.getUsers();
                    this.getComment(this.commentId);
                    reply.reset();
                },
                (error: HttpErrorResponse) => {
                  console.log(error.message);
                  this.getUsers();
                    this.getComment(this.commentId);
                  reply.reset();
                }
            );
        } 
    }

    onLikeComment(commentId: number): void{

        if (this.comment.isUserLiked === true) {
            this.comment.isUserLiked = !this.comment.isUserLiked;
            this.comment.commentLikes = this.comment.commentLikes - 1;
        }else{
            this.comment.isUserLiked = !this.comment.isUserLiked;
            this.comment.commentLikes = this.comment.commentLikes + 1;
        }

        this.commentService.likeComment(commentId).subscribe(
            (response: string) => {
                console.log(response);
                this.getUsers();
                this.getComment(this.commentId);
            },
            (error: HttpErrorResponse) => {
              console.log(error.message);
              this.getUsers();
              this.getComment(this.commentId);
            }
        );
    }

    onLikeCommentReply(commentReplyId: number): void{

        this.comment.commentReplyList.forEach( commentReply =>{
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

        this.commentReplyService.likeCommentReply(commentReplyId).subscribe(
            (response: string) => {
                console.log(response);
                this.getUsers();
                this.getComment(this.commentId);
            },
            (error: HttpErrorResponse) => {
              console.log(error.message);
              this.getUsers();
              this.getComment(this.commentId);
            }
        );
    }

    onDeleteComment(commentId: number): void{
      
        const data = new FormData()
        data.append('tweetId', String(this.tweetId));
        data.append('commentId', String(commentId));
        this.commentService.deleteComment(data).subscribe(
          (res: string) => {
            console.log(res);
            this.router.navigate(['/comment', this.tweetId]);
          },
          (error: HttpErrorResponse) => {
            console.log(error.message);
            this.router.navigate(['/comment', this.tweetId]);
          }
        );
    }

    onDeleteCommentReply(commentReplyId: number): void{

        const data = new FormData();
        data.append('commentId', String(this.comment.id));
        data.append('commentReplyId', String(commentReplyId));
        this.commentReplyService.deleteCommentReply(data).subscribe(
            (res: string) => {
                console.log(res);
                this.getUsers();
                this.getComment(this.commentId);
            },
            (error: HttpErrorResponse) => {
                console.log(error.message);
                this.getUsers();
                this.getComment(this.commentId);
            }
        );
    }

    onFollowUser(usernameOfUserToFollow: string): void{

        this.homeService.follow(usernameOfUserToFollow).subscribe(
            (res: string) => {
                console.log(res);
                this.getUsers();
                this.getComment(this.commentId);
            },
            (error: HttpErrorResponse) => {
                console.log(error.message);
                this.getUsers();
                this.getComment(this.commentId);
            }
        );
    }

    onMute(usernameToMute: string): void{
        this.homeService.mute(usernameToMute).subscribe(
            (res: string) => {
                console.log(res);
            },
            (error: HttpErrorResponse) => {
                console.log(error.message);
                this.getUsers();
                this.getComment(this.commentId);
            }
        );
    }

    onBlock(usernameToBlock: string): void{
        this.homeService.block(usernameToBlock).subscribe(
            (res: string) => {
                console.log(res);
            },
            (error: HttpErrorResponse) => {
                console.log(error.message);
                this.getUsers();
                this.getComment(this.commentId);
            }
        );
    }
}