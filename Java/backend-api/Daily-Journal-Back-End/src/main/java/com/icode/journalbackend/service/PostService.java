package com.icode.journalbackend.service;

import com.icode.journalbackend.exception.PostNotFoundException;
import com.icode.journalbackend.model.Post;
import com.icode.journalbackend.repo.PostRepository;
import com.icode.journalbackend.tool.TimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class PostService {

    private PostRepository postRepo;
    private TimeTool timeTool;

    @Autowired
    public PostService(PostRepository postRepo, TimeTool timeTool){
        this.postRepo = postRepo;
        this.timeTool = timeTool;
    }

    public List<Post> getPosts(){
        List<Post> posts = postRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        for (Post post: posts){
            String timePosted = post.getDatePosted()+" "+post.getTimePosted();
            String timeCurrently = LocalDate.now().toString()+" "+LocalTime.now().toString().substring(0,8);
            post.setTime(timeTool.getTimePosted(timePosted, timeCurrently));
        }

        return posts;
    }

    public Post getPostById(Long postId){
        return postRepo.findById(postId).orElseThrow(
                () -> new PostNotFoundException("Post with id of "+ postId +" was not found!.")
        );

    }

    public Post addPost(Post post){
        post.setTimePosted(LocalTime.now().toString().substring(0, 8));
        post.setDatePosted(LocalDate.now().toString());
        return postRepo.save(post);
    }

    public Post updatePost(Post post){
        return postRepo.save(post);
    }

    public void deletePostById(Long postId){
        postRepo.deleteById(postId);
    }
}
