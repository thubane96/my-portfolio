package com.icode.twitterclonebackend.controllers;

import com.icode.twitterclonebackend.models.User;
import com.icode.twitterclonebackend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@CrossOrigin(value = "http://localhost:4200", allowCredentials = "true")
public class ProfileController {

    private UserService userService;

    public ProfileController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(path = "/getUser/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId){
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(path = "/updateUser")
    public ResponseEntity<String> updateUser(
            @Nullable @RequestParam("profilePicture") MultipartFile profilePicture,
            @RequestParam("id") Long id, @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName, @RequestParam("bio") String bio,
            @RequestParam("location") String location, @RequestParam("birthDate") String birthDate){
        userService.updateUser(profilePicture, id, firstName, lastName, bio, location, birthDate);
        return new ResponseEntity<>("user updated", HttpStatus.OK);
    }

    @GetMapping(path = "/follow/{usernameOfUserToFollow}")
    public ResponseEntity<String> followUser(@PathVariable("usernameOfUserToFollow") String usernameOfUserToFollow,
                                             Principal principal){
        userService.followUser(principal.getName(), usernameOfUserToFollow);
        return new ResponseEntity<>("user followed", HttpStatus.OK);
    }

    @GetMapping(path = "/mute/{usernameToMute}")
    public ResponseEntity<String> mute(@PathVariable("usernameToMute") String usernameToMute, Principal principal){
        userService.muteUser(principal.getName(), usernameToMute);
        return new ResponseEntity<>("user muted", HttpStatus.OK);
    }

    @GetMapping(path = "/block/{usernameToBlock}")
    public ResponseEntity<String> block(@PathVariable("usernameToBlock") String usernameToBlock, Principal principal){
        userService.blockUser(principal.getName(), usernameToBlock);
        return new ResponseEntity<>("user blocked", HttpStatus.OK);
    }


















}
