package com.icode.workspace.netchatv2.controllers;

import com.icode.workspace.netchatv2.models.Messages;
import com.icode.workspace.netchatv2.models.Notifications;
import com.icode.workspace.netchatv2.services.FriendService;
import com.icode.workspace.netchatv2.services.MessagingService;
import com.icode.workspace.netchatv2.services.NotificationService;
import com.icode.workspace.netchatv2.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class SettingsController {

    private UserServiceImpl userService;
    private MessagingService messagingService;
    private FriendService friendService;
    private NotificationService notificationService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SettingsController(UserServiceImpl userService, FriendService friendService,
                                NotificationService notificationService, MessagingService messagingService){
        this.userService = userService;
        this.friendService = friendService;
        this.notificationService = notificationService;
        this.messagingService = messagingService;
    }

    @GetMapping(path = "/settings")
    public String settings(Model model, Principal principal){

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
        model.addAttribute("notifications", notifications);
        model.addAttribute("ListOfUsers", userService.getAllUsers());
        model.addAttribute("allMessages", messages);
        model.addAttribute("listOfFriendRequests", friendService.getFriendRequests(principal.getName()));

        return "settings";
    }

    @PostMapping(path = "/updateUserDetails")
    public String updateUserDetails(@RequestParam String firstName, @RequestParam String lastName,
                                    @RequestParam String email, Principal principal){

        String response = "success";

        boolean isUserFound = userService.getAllUsers().stream()
                .anyMatch(user -> email.equals(user.getUsername()));
        if (isUserFound){
            if (userService.getUserByUsername(principal.getName()).equals(email)){
                userService.updateUserDetails(firstName, lastName, email, principal.getName());
            }
            response = "error";
        }

        if(!isUserFound){
            userService.updateUserDetails(firstName, lastName, email, principal.getName());
        }

        return "redirect:/settings?"+response;
    }

    @PostMapping(path = "/updateProfilePicture")
    public String updateProfilePicture(@RequestParam MultipartFile img, Principal principal){
        userService.updateProfilePicture(principal.getName(), img);
        return "redirect:/settings?success";
    }

    @PostMapping(path = "/updatePassword")
    public String updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword01,
                                 @RequestParam String newPassword02, Principal principal){
        String response = null;

        String regex = "^(?=.*[0-9])" +
                "(?=.*[a-z])(?=.*[A-Z])" +
                "(?=.*[@#$%^&+=])"+
                "(?=\\S+$).{8,20}$";

        Pattern p = Pattern.compile(regex);

        if (passwordEncoder.matches(oldPassword, newPassword01)) {
            if (newPassword01.equals(newPassword02)) {
                Matcher m = p.matcher(newPassword01);
                if (m.matches()) {
                    userService.updatePassword(principal.getName(), oldPassword, newPassword01, newPassword02);
                    response = "success";
                }
                else if(!m.matches()){
                    response = "passwordNotSafe";
                }
            }else if (!newPassword01.equals(newPassword02)){
                response = "errorNewPasswordsDontMatch";
            }
        }else{
            response = "errorOldPasswordDontMatch";
        }

        return "redirect:/settings?"+response;
    }

}
