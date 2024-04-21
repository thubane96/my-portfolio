package com.icode.workspace.netchatv2.services;

import com.icode.workspace.netchatv2.apptools.ApplicationTools;
import com.icode.workspace.netchatv2.dto.CommentDto;
import com.icode.workspace.netchatv2.dto.PostDto;
import com.icode.workspace.netchatv2.exception.CommentNotFoundException;
import com.icode.workspace.netchatv2.exception.PostNotFoundException;
import com.icode.workspace.netchatv2.exception.UserLikedNotFound;
import com.icode.workspace.netchatv2.exception.UserNotFoundException;
import com.icode.workspace.netchatv2.models.*;
import com.icode.workspace.netchatv2.repositories.*;
import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
public class PostService {

    private PostRepository postRepo;
    private UserRepository userRepo;
    private CommentRepository commentRepo;
    private CommentLikedByRepository commentLikedByRepo;
    private PostLikedByRepository postLikedByRepo;
    private NotificationService notificationService;
    private ApplicationTools tool;

    List<Posts> getPostsList = new ArrayList<Posts>();
    List<Posts> getPosts = new ArrayList<Posts>();

    @Autowired
    public PostService(PostRepository postRepo, UserRepository userRepo,CommentRepository commentRepo,
                       CommentLikedByRepository commentLikedByRepo, PostLikedByRepository postLikedByRepo,
                       NotificationService notificationService, ApplicationTools tool){
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
        this.commentLikedByRepo = commentLikedByRepo;
        this.postLikedByRepo = postLikedByRepo;
        this.notificationService = notificationService;
        this.tool = tool;
    }

    public List<Posts> getAllPosts(String username){
        List<Posts> posts = postRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        Users user = userRepo.findByUsername(username);
        getPostsList.clear();
        for (Posts post: posts) {

            if (post.getAddedBy().equals(username)) {
                postFunction(post, username);
                getPostsList.add(post);
            } else if (!user.getFriends().isEmpty()) {
                for (Friends friend : user.getFriends()) {
                    if (post.getAddedBy().equals(friend.getFriendUsername())) {
                        postFunction(post, username);
                        getPostsList.add(post);
                    }
                }
            }

        }
        return getPostsList;
    }

    public void postFunction(Posts post, String username){

        int numberOfComments = 0;

        post.setTime(tool.getTimePosted(post.getTimeAdded(),
                LocalDate.now().toString()+" "+ LocalTime.now().toString().substring(0,8)));

        post.setUserLiked(false);
        if (!post.getPostLikedBy().isEmpty()) {
            for (PostLikedBy likedBy : post.getPostLikedBy()) {
                if (likedBy.getLikedBy().equals(username)) {
                    post.setUserLiked(true);
                    break;
                }
            }
        }

        if (post.getComments() != null){
            numberOfComments = post.getComments().size();
        }

        post.setNumberOfComments(numberOfComments);
        for (Comments comment: post.getComments()){
            comment.setTime(tool.getTimePosted(comment.getTimeCommented(),
                    LocalDate.now().toString()+" "+ LocalTime.now().toString().substring(0,8)));
            comment.setUserLiked(false);
            if (!comment.getCommentLikedBy().isEmpty()) {
                for (CommentLikedBy commentLikedBy : comment.getCommentLikedBy()) {
                    if (commentLikedBy.getLikedBy().equals(username)){
                        comment.setUserLiked(true);
                        break;
                    }
                }
            }
        }
    }

    public List<Posts> getUserPosts(String username){
        List<Posts> posts = postRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        getPosts.clear();
        for (Posts post: posts){
            if (post.getAddedBy().equals(username)) {
                postFunction(post, username);
                getPosts.add(post);
            }else if (post.getUserTo() != null){
                if (post.getUserTo().equals(username)){
                    postFunction(post, username);
                    getPosts.add(post);
                }
            }
        }
        return getPosts;
    }

    public void savePost(PostDto postPosted, String username){

        List<Url> found = new ArrayList<>();
        found.clear();

        //Detect youtube embedded video links in the post body
        UrlDetector parser = new UrlDetector(postPosted.getPostBody(), UrlDetectorOptions.Default);
        found = parser.detect();

        Posts post = new Posts();

        if (!found.isEmpty()){
            post.setVideoUrl(found.get(0).toString());
            postPosted.setPostBody(postPosted.getPostBody().replace(found.get(0).toString(), ""));
        }else if (found.isEmpty()){
            post.setBody(postPosted.getPostBody());
        }

        if (!postPosted.getImg().isEmpty()){
            try {
                post.setImg(Base64.getEncoder().encodeToString(postPosted.getImg().getBytes()));
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        if (postPosted.getUserProfileId() != null){
            Users userYouArePostingTo = userRepo.findById(postPosted.getUserProfileId()).orElseThrow(
                    () -> new UserNotFoundException("user with id of "+ postPosted.getUserProfileId()+" was not found!")
            );
            if (!userYouArePostingTo.getUsername().equals(username)) {
                post.setUserTo(userYouArePostingTo.getUsername());
            }
        }

        Users userThatIsPosting = userRepo.findByUsername(username);

        post.setAddedBy(username);
        post.setTimeAdded(LocalDate.now().toString()+" "+ LocalTime.now().toString().substring(0,8));
        post.setUserClosed(userThatIsPosting.getUserClosed());
        post.setDeleted("no");
        post.setLikes(0);
        postRepo.save(post);
    }

    public void deletePost(Long postId){
        postRepo.deleteById(postId);
    }

    public void likePost(Long postId, String username){

        Posts post = postRepo.findById(postId).orElseThrow(
                () -> new PostNotFoundException("Post with id of "+ postId +" was not found!.")
        );

        PostLikedBy postLikedBy = new PostLikedBy(username, post);


        if (!post.getPostLikedBy().isEmpty()){

            boolean isUserFound = post.getPostLikedBy().stream()
                    .anyMatch(user -> username.equals(user.getLikedBy()));

            if (!isUserFound) {
                post.setLikes(post.getLikes() + 1);

                post.addPostLikedBy(postLikedBy);
                postLikedByRepo.save(postLikedBy);

                savePostNotification(post, username, postId);

            }else  if (isUserFound){
                post.setLikes(post.getLikes() - 1);
                PostLikedBy postLikedByFound = post.getPostLikedBy()
                                            .stream()
                                            .filter(likedBy -> username.equals(likedBy.getLikedBy()))
                                            .findFirst()
                                            .orElseThrow( () -> new UserLikedNotFound("user with username of "+username+" did not like the post"));

                post.getPostLikedBy().remove(postLikedByFound);
                postRepo.save(post);

            }
        }else if (post.getPostLikedBy().isEmpty()){
            post.setLikes(post.getLikes() + 1);
            post.addPostLikedBy(postLikedBy);
            postLikedByRepo.save(postLikedBy);

            savePostNotification(post, username, postId);
        }

    }

    public void savePostNotification(Posts post, String username, Long postId){
        if (!post.getAddedBy().equals(username)) {
            Users user = userRepo.findByUsername(username);
            notificationService.saveNotification(
                    postId,
                    post.getAddedBy(),
                    username,
                    user.getFirstName() + " " + user.getLastName() + " liked your post.");
        }
    }

    public void saveComment(CommentDto comment, String username){

        Posts post = postRepo.findById(comment.getIdReceived()).orElseThrow(
                () -> new PostNotFoundException("Post with Id of "+ comment.getIdReceived() +" was not found!.")
        );

        Comments commentToBeSaved = new Comments(
                comment.getIdReceived(),
                comment.getCommentBody(),
                username,
                post.getAddedBy(),
                LocalDate.now().toString()+" "+ LocalTime.now().toString().substring(0,8),
                0,
                post.getDeleted(),
                post
        );

        post.getComments().add(commentToBeSaved);
        postRepo.save(post);

        //Save comment notification
        if (!post.getAddedBy().equals(username)) {
            Users user = userRepo.findByUsername(username);
            notificationService.saveNotification(
                    post.getId(),
                    post.getAddedBy(),
                    username,
                    user.getFirstName() + " " + user.getLastName() +  " commented on your post.");
        }

    }

    public void likeComment(Long commentId, String username){

        Comments comment = commentRepo.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("Comment with id of :"+ commentId +" was not found!.")
        );

        CommentLikedBy commentLikedBy = new CommentLikedBy(username, comment);


        if (!comment.getCommentLikedBy().isEmpty()){

            boolean foundUser = comment.getCommentLikedBy().stream()
                    .anyMatch(user -> username.equals(user.getLikedBy()));

            if (!foundUser) {
                comment.setNumOfLikes(comment.getNumOfLikes() + 1);
                comment.addCommentLikedBy(commentLikedBy);
                commentLikedByRepo.save(commentLikedBy);

                saveCommentNotification(comment, username, commentId);

            }else if(foundUser){
                comment.setNumOfLikes(comment.getNumOfLikes() - 1);
                CommentLikedBy commentLikedByFound = comment.getCommentLikedBy()
                        .stream()
                        .filter(likedBy -> username.equals(likedBy.getLikedBy()))
                        .findFirst()
                        .orElseThrow(
                                () -> new UserLikedNotFound("user with the username of "+username+" did like the comment!")
                        );
                comment.getCommentLikedBy().remove(commentLikedByFound);
                commentRepo.save(comment);
            }
        }else if (comment.getCommentLikedBy().isEmpty()){
            comment.setNumOfLikes(comment.getNumOfLikes() + 1);
            comment.addCommentLikedBy(commentLikedBy);
            commentLikedByRepo.save(commentLikedBy);

            saveCommentNotification(comment, username, commentId);
        }
    }

    public void saveCommentNotification(Comments comment, String username, Long commentId){
        Users user = userRepo.findByUsername(username);
        if (!comment.getCommentedBy().equals(username)) {
            notificationService.saveNotification(
                    comment.getPostId(),
                    comment.getCommentedBy(),
                    username,
                    user.getFirstName() + " " + user.getLastName() + " liked your comment.");
        }
    }

    public Posts getPost(Long postId){
        return postRepo.findById(postId).orElseThrow(
                () -> new PostNotFoundException("Post with id of "+ postId +" was not found!")
        );
    }

    public Comments getComment(Long commentId){
        return commentRepo.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("Comment with id of "+commentId+" was not found!.")
        );
    }

    public Users getPostedBy(Long postId){
        return userRepo.findByUsername(getPost(postId).getAddedBy());
    }

    public void deleteComment(Long commentId){
        commentRepo.deleteById(commentId);
    }

}
