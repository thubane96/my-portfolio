package com.icode.twitterclonebackend.controllers;

import com.icode.twitterclonebackend.models.Comment;
import com.icode.twitterclonebackend.models.Notifications;
import com.icode.twitterclonebackend.models.Tweet;
import com.icode.twitterclonebackend.models.User;
import com.icode.twitterclonebackend.services.TweetService;
import com.icode.twitterclonebackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:4200", allowCredentials = "true")
public class HomePageController {

    private TweetService tweetService;

    @Autowired
    private UserService userService;

    @Autowired
    public HomePageController(TweetService tweetService){
        this.tweetService = tweetService;
    }

    @PostMapping(path = "/saveTweet")
    public ResponseEntity<String> saveTweet(@Nullable @RequestParam("tweetBody") String tweetBody,
                                            @Nullable @RequestParam("tweetImage") MultipartFile tweetImage,
                                            Principal principal){

        tweetService.saveTweet(tweetBody, tweetImage, principal.getName());

        return new ResponseEntity<>("true", HttpStatus.CREATED);
    }

    @GetMapping(path = "/getUsers")
    public ResponseEntity<List<User>> getUsers(Principal principal){
        List<User> users = userService.getUsers(principal.getName());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/getTweets")
    public ResponseEntity<List<Tweet>> getTweets(Principal principal){
        List<Tweet> tweets = tweetService.fetchTweets(principal.getName());
        return new ResponseEntity<>(tweets, HttpStatus.OK);
    }

    @GetMapping(path = "/getAllTweets")
    public ResponseEntity<List<Tweet>> getAllTweets(Principal principal){
        List<Tweet> tweets = tweetService.getAllTweets(principal.getName());
        return new ResponseEntity<>(tweets, HttpStatus.OK);
    }

    @GetMapping(path = "/getBookmarkedTweets")
    public ResponseEntity<List<Tweet>> getBookmarkedTweets(Principal principal){
        List<Tweet> bookmarkedTweets = tweetService.getBookmarks(principal.getName());
        return new ResponseEntity<>(bookmarkedTweets, HttpStatus.OK);
    }

    @GetMapping(path = "/getWidgetTweets")
    public ResponseEntity<List<Tweet>> getWidgetTweets(Principal principal){
        List<Tweet> widgetTweets = tweetService.getWidgetTweets(principal.getName());
        return new ResponseEntity<>(widgetTweets, HttpStatus.OK);
    }

    @GetMapping(path = "/likeTweet/{tweetId}")
    public ResponseEntity<String> likeTweet(@PathVariable("tweetId") Long tweetId, Principal principal){
        tweetService.likeTweet(tweetId, principal.getName());
        return new ResponseEntity<>("tweet liked", HttpStatus.OK);
    }

    @GetMapping(path = "/getTweet/{tweetId}")
    public ResponseEntity<Tweet> getTweet(@PathVariable("tweetId") Long tweetId, Principal principal){
        Tweet tweet = tweetService.fetchTweet(tweetId, principal.getName());
        return new ResponseEntity<>(tweet, HttpStatus.OK);
    }

    @PostMapping(path = "/addComment")
    public ResponseEntity<String> addComment(@RequestParam("tweetId") String tweetId,
                                             @Nullable @RequestParam("commentBody") String commentBody,
                                             @Nullable @RequestParam("commentImage") MultipartFile commentImage,
                                             Principal principal){

        tweetService.addComment(Long.parseLong(tweetId), commentBody, commentImage, principal.getName());
        return new ResponseEntity<>("comment added", HttpStatus.CREATED);
    }



    @GetMapping(path = "/likeComment/{commentId}")
    public ResponseEntity<String> likeComment(@PathVariable("commentId") Long commentId, Principal principal){
        tweetService.likeComment(commentId, principal.getName());
        return new ResponseEntity<>("comment liked", HttpStatus.OK);
    }


    @GetMapping(path = "/getComment/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable("commentId") Long commentId, Principal principal){
        Comment comment = tweetService.getComment(commentId, principal.getName());
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PostMapping(path = "/addCommentReply")
    public ResponseEntity<String> addCommentReply(@RequestParam("commentId") String commentId,
                                             @Nullable @RequestParam("commentReplyBody") String commentReplyBody,
                                             @Nullable @RequestParam("commentReplyImage") MultipartFile commentReplyImage,
                                             Principal principal){

        tweetService.addCommentReply(Long.parseLong(commentId), commentReplyBody, commentReplyImage, principal.getName());
        return new ResponseEntity<>("comment reply added", HttpStatus.CREATED);
    }



    @GetMapping(path = "/likeCommentReply/{commentReplyId}")
    public ResponseEntity<String> likeCommentReply(@PathVariable("commentReplyId") Long commentReplyId,
                                                   Principal principal){
        tweetService.likeCommentReply(commentReplyId, principal.getName());
        return new ResponseEntity<>("comment reply liked", HttpStatus.OK);
    }

    @GetMapping(path = "/deleteTweet/{tweetId}")
    public ResponseEntity<String> deleteTweet(@PathVariable("tweetId") Long tweetId, Principal principal){
        tweetService.deleteTweet(tweetId, principal.getName());
        String res = "tweet with id of "+ tweetId +" was deleted";
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(path = "/deleteComment")
    public ResponseEntity<String> deleteComment(@RequestParam("tweetId") Long tweetId,
                                                @RequestParam("commentId") Long commentId,
                                                Principal principal){
        tweetService.deleteComment(tweetId, commentId, principal.getName());
        String res = "comment with id of "+commentId+" was deleted";
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(path = "/deleteCommentReply")
    public ResponseEntity<String> deleteCommentReply(@RequestParam("commentId") Long commentId,
                                                @RequestParam("commentReplyId") Long commentReplyId,
                                                Principal principal){
        tweetService.deleteCommentReply(commentId, commentReplyId, principal.getName());
        String res = "comment reply with id of "+commentId+" was deleted";
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @GetMapping(path = "/pinTweet/{tweetId}")
    public ResponseEntity<String> pinTweet(@PathVariable("tweetId") Long tweetId, Principal principal){
        tweetService.addPinnedTweet(tweetId, principal.getName());
        return new ResponseEntity<>("tweet pinned", HttpStatus.OK);
    }


    @GetMapping(path = "/addTweetToList/{tweetId}")
    public ResponseEntity<String> addTweetToList(@PathVariable("tweetId") Long tweetId, Principal principal){
        tweetService.addTweetToList(tweetId, principal.getName());
        return new ResponseEntity<>("tweet added to list", HttpStatus.OK);
    }

    @GetMapping(path = "/hideTweet/{tweetId}")
    public ResponseEntity<String> hideTweet(@PathVariable("tweetId") Long tweetId, Principal principal){
        tweetService.hideTweet(tweetId, principal.getName());
        return new ResponseEntity<>("tweet hidden", HttpStatus.OK);
    }

   @GetMapping(path = "/bookmark/{tweetId}")
    public ResponseEntity<String> bookmark(@PathVariable("tweetId") Long tweetId, Principal principal){
        tweetService.bookmarkTweet(tweetId, principal.getName());
        return new ResponseEntity<>("tweet bookmarked", HttpStatus.OK);
   }

   @GetMapping(path = "/notInterested/{tweetId}")
    public ResponseEntity<String> notInterestedInTweet(@PathVariable("tweetId") Long tweetId, Principal principal){
        tweetService.putTweetOnNotInterestedList(tweetId, principal.getName());
        return new ResponseEntity<>("tweet added on tweet not interested list", HttpStatus.OK);
   }


    @GetMapping(path = "/getNotifications")
    public ResponseEntity<List<Notifications>> getAllNotifications(){
        List<Notifications> notifications = tweetService.getNotifications();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping(path = "/openNotification/{notificationId}")
    public ResponseEntity<String> openNotification(@PathVariable("notificationId") Long notificationId){
        tweetService.openNotification(notificationId);
        return new ResponseEntity<>("notification opened", HttpStatus.OK);
    }














}
