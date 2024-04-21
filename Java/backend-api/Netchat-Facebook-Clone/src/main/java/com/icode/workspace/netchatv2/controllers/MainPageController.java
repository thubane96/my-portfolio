package com.icode.workspace.netchatv2.controllers;

import com.icode.workspace.netchatv2.dto.CommentDto;
import com.icode.workspace.netchatv2.dto.FriendRequestDto;
import com.icode.workspace.netchatv2.dto.PostDto;
import com.icode.workspace.netchatv2.dto.RejectFriendRequestDto;
import com.icode.workspace.netchatv2.models.*;
import com.icode.workspace.netchatv2.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class MainPageController {

    private UserServiceImpl userService;
    private PostService postService;
    private MessagingService messagingService;
    private FriendService friendService;
    private NotificationService notificationService;

    public MainPageController(UserServiceImpl userService, FriendService friendService, PostService postService,
                              NotificationService notificationService, MessagingService messagingService){
        this.userService = userService;
        this.postService = postService;
        this.friendService = friendService;
        this.notificationService = notificationService;
        this.messagingService = messagingService;
    }

    @ModelAttribute("post")
    public PostDto postDto() {
        return new PostDto();
    }

    @ModelAttribute("comment")
    public CommentDto commentDto() {
        return new CommentDto();
    }

    @ModelAttribute("friendRequest")
    public FriendRequestDto friendRequestDto() {
        return new FriendRequestDto();
    }

    @ModelAttribute("rejectRequest")
    public RejectFriendRequestDto rejectFriendRequestDto(){
        return new RejectFriendRequestDto();
    }

    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }

    @GetMapping("/")
    public String homePage(Principal principal, Model model){

        Users user = userService.getUserByUsername(principal.getName());
        List<FriendRequests> friendRequests = friendService.getFriendRequests(principal.getName());
        List<Posts> userPosts = postService.getUserPosts(principal.getName());
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



        model.addAttribute("user", userService.getUserByUsername(principal.getName()));
        model.addAttribute("numberOfMessages", messagingService.getNumberOfMessages(principal.getName()));
        model.addAttribute("listOfFriendRequests", friendRequests);
        model.addAttribute("numOfFriendRequests", friendRequests.size());
        model.addAttribute("posts", postService.getAllPosts(principal.getName()));
        model.addAttribute("ListOfUsers", userService.getAllUsers());
        model.addAttribute("numberOfNotifications", notificationService.getNotifications(principal.getName()).size());
        model.addAttribute("allMessages", messages);
        model.addAttribute("notifications", notificationService.getNotifications(principal.getName()));
        model.addAttribute("numPosts", userPosts.size());
        model.addAttribute("numOfLikes", numberOfLikes);
        model.addAttribute("numOfFriends", user.getFriends().size());
        return "index.html ";
    }

    @GetMapping(path = "/acceptFriendRequest/{requestId}")
    public String acceptFriendRequest(@PathVariable("requestId") Long requestId){
        friendService.addFriend(requestId);
        return "redirect:/";
    }

    @GetMapping(path = "/rejectFriendRequest/{requestId}")
    public String rejectFriendRequest(@PathVariable("requestId") Long requestId){
        friendService.rejectFriendRequest(requestId);
        return "redirect:/";
    }


    @PostMapping(path ="/post")
    public String savePost(@ModelAttribute("post") PostDto postDto, Principal principal) throws IOException {

        postService.savePost(postDto, principal.getName());

        return "redirect:/?success";
    }

    @GetMapping(path="/like/{postId}")
    public String likePost(@PathVariable("postId") Long postId, Principal principal) {

        postService.likePost(postId, principal.getName());

        String message = principal.getName() +" liked your post";


        return "redirect:/";
    }

    @GetMapping(path = "/likeComment/{commentId}")
    public String likeComment(@PathVariable("commentId") Long commentId, Principal principal){
        postService.likeComment(commentId, principal.getName());
        return "redirect:/";
    }

    @PostMapping(path = "/comment")
    public String comment(@ModelAttribute("comment") CommentDto commentDto,
                          Principal principal){
        postService.saveComment(commentDto, principal.getName());
        return "redirect:/?success";
    }

    @GetMapping(path = "/deletePost/{postId}")
    public String deletePost(@PathVariable("postId") Long postId){
        postService.deletePost(postId);
        return "redirect:/?success";
    }

    @GetMapping(path = "/deleteComment/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId){
        postService.deleteComment(commentId);
        return "redirect:/?success";
    }


}
