import { ProfileComponent } from './profile/profile.component';
import { CommentComponent } from './comment/comment.component';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpInterceptorService } from './auth/http-interceptor.service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatMenuModule} from '@angular/material/menu';
import {MatIconModule} from '@angular/material/icon';
import {MatTabsModule} from '@angular/material/tabs';
import {ScrollingModule} from '@angular/cdk/scrolling';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatInputModule} from '@angular/material/input';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatBadgeModule} from '@angular/material/badge';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { WidgetComponent } from './widget/widget.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { CommentReplyComponent } from './comment_reply/comment-reply.component';
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { BookmarkComponent } from './bookmark/bookmark.component';
import { ListComponent } from './list/list.component';
import { FollowComponent } from './follow/follow.component';
import { SearchComponent } from './search/search.component';
import { MessagesComponent } from './messages/messages.component';
import { MessageComponent } from './message/message.component';
import { SearchUserPipe } from './search/user-search.pipe';
import { TweetSearchPipe } from './search/tweet-search.pipe';
import { NotificationComponent } from './notification/notification.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    WidgetComponent,
    CommentComponent,
    ProfileComponent,
    CommentReplyComponent,
    EditProfileComponent,
    BookmarkComponent,
    ListComponent,
    FollowComponent,
    SearchComponent,
    MessagesComponent,
    MessageComponent,
    SearchUserPipe,
    TweetSearchPipe,
    NotificationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatSidenavModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatProgressBarModule,
    MatMenuModule,
    MatTabsModule,
    ScrollingModule,
    MatExpansionModule,
    MatInputModule,
    MatDatepickerModule,
    MatBadgeModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
