import { LoginService } from './../login/login.service';
import { AppService } from './../app.service';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { User } from '../model/DataModel';
import { EditProfileService } from './edit-profile.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss']
})
export class EditProfileComponent implements OnInit {

  user: User;
  userId: number;
  username: string;
  selectedFile: File;

  constructor(
    private editProfileService: EditProfileService,
    private appService: AppService,
    private loginService: LoginService,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get("userId"));
    this.getUsername();
    this.getUser();
  }

  getUser(){
    this.editProfileService.getUsers().subscribe(
      (res: User[]) => {
        res.forEach( user => {
          if( user.id === this.userId){
            console.log(user.profilePicture);
            this.user = user;
            if (this.user.profilePicture !== undefined){
               this.user.profilePicture = 'data:image/jpeg;base64,'+this.user.profilePicture;
            }else {
              this.user.profilePicture = "/assets/img/profile_pic.jpg";
            }
           
          }else if( user.username === this.username){
            this.appService.userId.emit(user.id);
          }
    
        });
        
        this.appService.userId.emit(this.user.id);
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    );
  }

  getUsername(): void{
    this.username = this.loginService.getSignedinUser();
  }

  onUpdateUser(user: User): void{
    const userData = new FormData();
    if(this.selectedFile !== undefined){
      userData.append("profilePicture", this.selectedFile);
    }
    userData.append("id", String(user.id))
    userData.append("firstName", user.firstName);
    userData.append("lastName", user.lastName);
    userData.append("bio",user.bio);
    userData.append("location", user.location);
    userData.append("birthDate", user.birthDate);
    user.profilePicture = this.selectedFile;
    console.log(user.profilePicture);
    this.editProfileService.updateUser(userData).subscribe(
      (res: string) => {
        console.log(res);
        this.getUser();
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
        this.getUser();
      }
    );
  }

  public onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

}
