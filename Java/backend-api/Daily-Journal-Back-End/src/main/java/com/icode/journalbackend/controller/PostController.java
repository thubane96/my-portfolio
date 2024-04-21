package com.icode.journalbackend.controller;

import com.icode.journalbackend.model.Post;
import com.icode.journalbackend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/post")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Post>> getPosts(){
        List<Post> posts = postService.getPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping(path = "/get/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable("postId") Long postId){
        Post fetchedPost = postService.getPostById(postId);
        return new ResponseEntity<>(fetchedPost, HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Post> addPost(@RequestBody Post post){
        Post addedPost = postService.addPost(post);
        return new ResponseEntity<>(addedPost, HttpStatus.CREATED);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<Post> updatePost(@RequestBody Post post){
        Post updatedPost = postService.updatePost(post);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId){
        postService.deletePostById(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
