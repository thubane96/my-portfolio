import { MessageComponent } from './message/message.component';
import { MessagesComponent } from './messages/messages.component';
import { NotificationComponent } from './notification/notification.component';
import { SearchComponent } from './search/search.component';
import { FollowComponent } from './follow/follow.component';
import { BookmarkComponent } from './bookmark/bookmark.component';
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { CommentComponent } from './comment/comment.component';
import { ProfileComponent } from './profile/profile.component';
import { HomeComponent } from './home/home.component';
import { NgModule, Component } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommentReplyComponent } from './comment_reply/comment-reply.component';

const routes: Routes = [
  // {path: '', component: LoginComponent},
  {path: '', component: HomeComponent},
  {path: 'comment/:tweetId', component: CommentComponent},
  {path: 'profile/:userId', component: ProfileComponent},
  {path: 'commentReply/:commentId/:tweetId', component: CommentReplyComponent},
  {path: 'editProfile/:userId', component: EditProfileComponent},
  {path: 'bookmarks', component: BookmarkComponent},
  {path: 'follow/:userId', component: FollowComponent},
  {path: 'search', component: SearchComponent},
  {path: 'notification', component: NotificationComponent},
  {path: 'messages', component: MessagesComponent},
  {path: 'message/:friendUserId/:where', component: MessageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
