package com.icode.workspace.netchatv2.controllers;

import com.icode.workspace.netchatv2.models.Messages;
import com.icode.workspace.netchatv2.models.Notifications;
import com.icode.workspace.netchatv2.services.FriendService;
import com.icode.workspace.netchatv2.services.MessagingService;
import com.icode.workspace.netchatv2.services.NotificationService;
import com.icode.workspace.netchatv2.services.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class SearchUserController {

    private UserServiceImpl userService;
    private MessagingService messagingService;
    private FriendService friendService;
    private NotificationService notificationService;

    public SearchUserController(UserServiceImpl userService, FriendService friendService,
                              NotificationService notificationService, MessagingService messagingService){
        this.userService = userService;
        this.friendService = friendService;
        this.notificationService = notificationService;
        this.messagingService = messagingService;
    }

    @GetMapping(path = "/getSearchResults")
    public String showSearchResult(@RequestParam String keywords, Model model, Principal principal){

        List<Notifications> notifications = notificationService.getNotifications(principal.getName());
        List<Messages> messages = messagingService.getAllMessages();

        for(Messages message: messages){
            String oldMessage = null;
            if (message.getBody().length() > 15){
                oldMessage = message.getBody().substring(0, 16);
                message.setBody(oldMessage);
            }
        }

        model.addAttribute("user", userService.getUserByUsername(principal.getName()));
        model.addAttribute("numberOfMessages", messagingService.getNumberOfMessages(principal.getName()));
        model.addAttribute("numOfFriendRequests", friendService.getFriendRequests(principal.getName()).size());
        model.addAttribute("numberOfNotifications", notifications.size());
        model.addAttribute("searchResults", userService.getSearch(keywords));
        model.addAttribute("notifications", notifications);
        model.addAttribute("ListOfUsers", userService.getAllUsers());
        model.addAttribute("allMessages", messages);
        model.addAttribute("listOfFriendRequests", friendService.getFriendRequests(principal.getName()));
        return "search-result";
    }


}
