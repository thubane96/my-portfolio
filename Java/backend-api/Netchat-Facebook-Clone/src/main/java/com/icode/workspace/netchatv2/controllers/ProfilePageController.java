package com.icode.workspace.netchatv2.controllers;

import com.icode.workspace.netchatv2.dto.*;
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

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class ProfilePageController {

    private UserServiceImpl userService;
    private FriendService friendService;
    private PostService postService;
    private MessagingService messagingService;
    private NotificationService notificationService;

    private Long globalId;

    public ProfilePageController(UserServiceImpl userService, FriendService friendService, PostService postService,
            MessagingService messagingService, NotificationService notificationService){
        this.userService = userService;
        this.friendService = friendService;
        this.postService = postService;
        this.messagingService = messagingService;
        this.notificationService = notificationService;
    }


    @ModelAttribute("post")
    public PostDto postDto() {
        return new PostDto();
    }

    @ModelAttribute("comment")
    public CommentDto commentDto() {
        return new CommentDto();
    }

    @ModelAttribute("deletePost")
    public PostDeleteDto postDeleteDto() {
        return new PostDeleteDto();
    }

    @ModelAttribute("friendRequest")
    public FriendRequestDto friendRequestDto() {
        return new FriendRequestDto();
    }

    @ModelAttribute("rejectRequest")
    public RejectFriendRequestDto rejectFriendRequestDto(){
        return new RejectFriendRequestDto();
    }

    @ModelAttribute("message")
    public MessageDto messageDto(){
        return new MessageDto();
    }

    @GetMapping(path = "/profile/{userId}")
    public String getProfilePage(@PathVariable("userId")  Long userId, Model model, Principal principal) {

        Users userClickedProfile = userService.getUsersDetails(userId);
        Users user = userService.getUserByUsername(principal.getName());
        List<FriendRequests> getFriendRequests = friendService.getFriendRequests(principal.getName());
        List<Posts> posts = postService.getUserPosts(userClickedProfile.getUsername());
        List<Messages> allMessages = messagingService.getMessages(userClickedProfile.getUsername(), principal.getName());

        for (Messages message: allMessages){
            String oldMessage = null;
            if (message.getBody().length() > 15) {
                oldMessage = message.getBody().substring(0, 16) + "...";
                message.setBody(oldMessage);
            }
        }
        int numberOfLikes = 0;
        for (Posts post: posts){
            numberOfLikes += post.getLikes();
        }

        globalId = userId;

        model.addAttribute("isRequestSent", friendService.isRequestSent(userId, principal.getName()));
        model.addAttribute("friendRequestDetails", friendService.friendRequestsDetails(principal.getName()));
        model.addAttribute("isRequestSentToYou", friendService.isRequestSent(user.getId(), userClickedProfile.getUsername()));
        model.addAttribute("userInfoOfCurrentUser", user);
        model.addAttribute("numberOfMessages", messagingService.getNumberOfMessages(principal.getName()));
        model.addAttribute("numOfFriendRequests", getFriendRequests.size());
        model.addAttribute("numberOfNotifications", notificationService.getNotifications(principal.getName()).size());
        model.addAttribute("user", userClickedProfile);
        model.addAttribute("numPosts", posts.size());
        model.addAttribute("numOfLikes", numberOfLikes);
        model.addAttribute("numOfFriends", userClickedProfile.getFriends().size());
        model.addAttribute("isFriend", friendService.isFriend(user, userClickedProfile.getUsername()));
        model.addAttribute("posts", posts);
        model.addAttribute("ListOfUsers", userService.getAllUsers());
        model.addAttribute("isYourProfile", principal.getName().equals(userClickedProfile.getUsername()));
        model.addAttribute("messages", messagingService.getAllMessages());
        model.addAttribute("allMessages", allMessages);
        model.addAttribute("listOfFriends", userClickedProfile.getFriends());
        model.addAttribute("listOfFriendRequests", getFriendRequests);
        model.addAttribute("notifications", notificationService.getNotifications(principal.getName()));

        return "profile";
    }

    @PostMapping(path ="/postProfile")
    public String savePost(@ModelAttribute("post")PostDto postDto, Principal principal) throws IOException {
        postService.savePost(postDto, principal.getName());
        return "redirect:/profile/"+globalId+"?success";
    }

    @GetMapping(path="/likeInProfile/{postId}")
    public String likePostInProfile(@PathVariable("postId") Long postId, Principal principal) {
        postService.likePost(postId, principal.getName());

        return "redirect:/profile/"+globalId;
    }


    @GetMapping(path = "/likeCommentInProfile/{commentId}")
    public String likeCommentInProfile(@PathVariable("commentId") Long commentId, Principal principal){
        postService.likeComment(commentId, principal.getName());
        return "redirect:/profile/"+globalId;
    }

    @PostMapping(path = "/commentProfile")
    public String comment(@ModelAttribute("comment") CommentDto commentDto,
                          Principal principal){

        postService.saveComment(commentDto, principal.getName());

        return "redirect:/profile/"+globalId;
    }

    @GetMapping(path = "/deletePostProfile/{postId}")
    public String deletePost(@PathVariable("postId") Long postId){
        postService.deletePost(postId);
        System.out.println("Post with id of "+ postId +" was deleted");
        return "redirect:/profile/"+globalId;
    }

    @GetMapping(path = "/inviteFriend/{userId}")
    public String inviteFriend(@PathVariable("userId") Long userId, Principal principal){
        friendService.addFriendRequest(userId, principal.getName());
        return "redirect:/profile/" + globalId;
    }

    @GetMapping(path = "/cancelFriendRequest/{userId}")
    public String cancelFriendRequest(@PathVariable("userId") Long userId, Principal principal){
        friendService.cancelRequest(userId, principal.getName());
        return "redirect:/profile/"+ globalId;
    }

    @GetMapping(path = "/profile/acceptFriendRequest/{requestId}")
    public String acceptFriendRequest(@PathVariable("requestId") Long requestId){
        friendService.addFriend(requestId);
        return "redirect:/profile/"+ globalId;
    }

    @GetMapping(path = "/profile/rejectFriendRequest/{requestId}")
    public String rejectFriendRequest(@PathVariable("requestId") Long requestId){
        friendService.rejectFriendRequest(requestId);
        return "redirect:/profile/"+globalId;
    }

    @GetMapping(path = "/removeFriend/{friendId}")
    public String removeFriend(@PathVariable("friendId") Long friendId, Principal principal){
        friendService.removeFriend(principal.getName(), friendId);
        return "redirect:/profile/"+globalId;
    }

    @GetMapping(path = "/deleteCommentInProfile/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId){
        postService.deleteComment(commentId);
        return "redirect:/profile/"+globalId;
    }

}
