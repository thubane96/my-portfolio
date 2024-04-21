package com.icode.twitterclonebackend.controllers;

import com.icode.twitterclonebackend.services.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@CrossOrigin(value = "http://localhost:4200", allowCredentials = "true")
public class MessagingController {

    @Autowired
    private MessagingService messagingService;

    @PostMapping(path = "/saveMessage")
    public ResponseEntity<String> saveMessage(@RequestParam("userToUsername") String userToUsername,
                                              @Nullable @RequestParam("messageBody") String messageBody,
                                              @Nullable @RequestParam("messageImage") MultipartFile messageImage,
                                              Principal principal){
        messagingService.saveMessage(userToUsername, messageBody, messageImage, principal.getName());
        return new ResponseEntity<>("message sent", HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/openMessage/{friendUsername}")
    public ResponseEntity<String> openMessage(@PathVariable("friendUsername") String friendUsername,
                                              Principal principal){
        messagingService.openMessage(friendUsername, principal.getName());
        return new ResponseEntity<>("Messages opened", HttpStatus.OK);
    }


}
