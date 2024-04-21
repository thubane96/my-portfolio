import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { faCoffee, faEdit } from '@fortawesome/free-solid-svg-icons';
import { Post } from './post';
import { PostService } from './post.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  title = 'journal-frontend';
  faEdit = faEdit;
  public posts: Post[];
  public editPost: Post;
  public deletePost: Post;

  constructor(private postService: PostService){}

  ngOnInit(): void {
    this.getPosts();
  }

  public getPosts(): void {
    this.postService.getPosts().subscribe(
      (response: Post[]) => {
        this.posts = response;
        console.log(this.posts);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onAddPost(addForm: NgForm): void {
    this.postService.addPost(addForm.value).subscribe(
      (response: Post) => {
        console.log(response);
        this.getPosts();
        addForm.reset();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onUpdatePost(post: Post): void {
    this.postService.updatePost(post).subscribe(
      (response: Post) => {
        console.log(response);
        this.getPosts();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onDeletePost(postId: number): void {
    this.postService.deletePost(postId).subscribe(
      (response: void) => {
        console.log(response);
        this.getPosts();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onOpenModal(post: Post, mode: string): void {
    const container = document.getElementById('container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'edit') {
      this.editPost = post;
      button.setAttribute('data-target', '#editComposeModal');
    }
    if (mode === 'delete') {
      this.deletePost = post;
      button.setAttribute('data-target', '#deleteComposeModal');
    }
    container.appendChild(button);
    button.click();
  }
  
}
