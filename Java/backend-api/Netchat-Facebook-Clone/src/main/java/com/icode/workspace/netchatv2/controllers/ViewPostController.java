package com.icode.workspace.netchatv2.controllers;

import com.icode.workspace.netchatv2.dto.CommentDto;
import com.icode.workspace.netchatv2.models.FriendRequests;
import com.icode.workspace.netchatv2.models.Messages;
import com.icode.workspace.netchatv2.models.Posts;
import com.icode.workspace.netchatv2.models.Users;
import com.icode.workspace.netchatv2.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ViewPostController {

    private UserServiceImpl userService;
    private MessagingService messagingService;
    private NotificationService notificationService;
    private PostService postService;
    private FriendService friendService;

    private Long globalId;

    public ViewPostController(UserServiceImpl userService, MessagingService messagingService,
                              NotificationService notificationService, PostService postService,
                              FriendService friendService){
        this.userService = userService;
        this.messagingService = messagingService;
        this.notificationService = notificationService;
        this.postService = postService;
        this.friendService = friendService;
    }

    @ModelAttribute("comment")
    public CommentDto commentDto() {
        return new CommentDto();
    }


    @GetMapping(path = "viewPost/{postId}/{notificationId}")
    public String viewPost(@PathVariable("postId") Long postId, @PathVariable("notificationId") Long notificationId,
                           Model model, Principal principal) {

        notificationService.removeNotification(notificationId);

        Users getPosted = postService.getPostedBy(postId);
        List<Posts> userPosts = postService.getUserPosts(principal.getName());
        List<FriendRequests> getFriendRequests = friendService.getFriendRequests(getPosted.getUsername());
        List<Messages> messages = messagingService.getAllMessages();

        for (Messages message: messages){
            String oldMessage = null;
            if (message.getBody().length() > 15) {
                oldMessage = message.getBody().substring(0, 16) + "...";
                message.setBody(oldMessage);
            }
        }

        int numberOfLikes = 0;
        for (Posts post: userPosts){
            numberOfLikes += post.getLikes();
        }

        globalId = postId;

        model.addAttribute("user", userService.getUserByUsername(principal.getName()));
        model.addAttribute("post", postService.getPost(postId));
        model.addAttribute("postedBy", getPosted);
        model.addAttribute("numberOfLikes", numberOfLikes);
        model.addAttribute("numberOfPosts", userPosts.size());
        model.addAttribute("numberOfFriends", getPosted.getFriends().size());
        model.addAttribute("ListOfUsers", userService.getAllUsers());
        model.addAttribute("numOfFriendRequests", getFriendRequests.size());
        model.addAttribute("listOfFriendRequests", getFriendRequests);
        model.addAttribute("notifications", notificationService.getNotifications(principal.getName()));
        model.addAttribute("allMessages", messages);
        model.addAttribute("numberOfMessages", messagingService.getNumberOfMessages(principal.getName()));
        model.addAttribute("numberOfNotifications", notificationService.getNotifications(principal.getName()).size());

        return "view-post";
    }

    @GetMapping(path = "viewPost/{postId}")
    public String viewPost2(@PathVariable("postId") Long postId, Model model, Principal principal){

        Users getPosted = postService.getPostedBy(postId);
        List<Posts> userPosts = postService.getUserPosts(principal.getName());
        List<FriendRequests> getFriendRequests = friendService.getFriendRequests(getPosted.getUsername());
        List<Messages> messages = messagingService.getAllMessages();

        for (Messages message: messages){
            String oldMessage = null;
            if (message.getBody().length() > 15) {
                oldMessage = message.getBody().substring(0, 16) + "...";
                message.setBody(oldMessage);
            }
        }

        int numberOfLikes = 0;
        for (Posts post: userPosts){
            numberOfLikes += post.getLikes();
        }

        globalId = postId;

        model.addAttribute("user", userService.getUserByUsername(principal.getName()));
        model.addAttribute("post", postService.getPost(postId));
        model.addAttribute("postedBy", getPosted);
        model.addAttribute("numberOfLikes", numberOfLikes);
        model.addAttribute("numberOfPosts", userPosts.size());
        model.addAttribute("numberOfFriends", getPosted.getFriends().size());
        model.addAttribute("ListOfUsers", userService.getAllUsers());
        model.addAttribute("numOfFriendRequests", getFriendRequests.size());
        model.addAttribute("listOfFriendRequests", getFriendRequests);
        model.addAttribute("notifications", notificationService.getNotifications(principal.getName()));
        model.addAttribute("allMessages", messages);
        model.addAttribute("numberOfMessages", messagingService.getNumberOfMessages(principal.getName()));
        model.addAttribute("numberOfNotifications", notificationService.getNotifications(principal.getName()).size());

        return "view-post";
    }

    @PostMapping(path = "/commentInViewPost")
    public String comment(@ModelAttribute("comment") CommentDto commentDto,
                          Principal principal){
        postService.saveComment(commentDto, principal.getName());
         return "redirect:/viewPost/"+commentDto.getIdReceived();
    }

    @GetMapping(path="/likeInViewPost/{postId}")
    public String likePost(@PathVariable("postId") Long postId, Principal principal) {
        postService.likePost(postId, principal.getName());
        return "redirect:/viewPost/"+postId;
    }

    @GetMapping(path = "/likeCommentInViewPost/{commentId}")
    public String likeCommentInProfile(@PathVariable("commentId") Long commentId, Principal principal){

        Users user = userService.getUserByUsername(principal.getName());
        postService.likeComment(commentId, principal.getName());

        return "redirect:/viewPost/"+postService.getComment(commentId).getPostId();
    }

    @GetMapping(path = "/deleteCommentInViewPost/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId){
        postService.deleteComment(commentId);
        return "redirect:/viewPost/"+globalId;
    }

}
