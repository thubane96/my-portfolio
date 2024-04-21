package com.icode.twitterclonebackend.services;

import com.icode.twitterclonebackend.exception.UserNotFollowException;
import com.icode.twitterclonebackend.exception.UserNotFoundException;
import com.icode.twitterclonebackend.models.*;
import com.icode.twitterclonebackend.repositories.*;
import com.icode.twitterclonebackend.util.TweetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepo;
    private FollowRepository followRepo;
    private BlockUserRepository blockUserRepo;
    private TweetMutedUserRepository tweetMutedUserRepo;
    private NotificationRepository notificationRepo;
    private TweetUtil tweetUtil;
    private MessagingService messagingService;

    @Autowired
    public UserService(UserRepository userRepo, FollowRepository followRepo, BlockUserRepository blockUserRepo,
                       TweetMutedUserRepository tweetMutedUserRepo, TweetUtil tweetUtil,
                       NotificationRepository notificationRepo, MessagingService messagingService){
        this.userRepo = userRepo;
        this.followRepo = followRepo;
        this.blockUserRepo = blockUserRepo;
        this.tweetUtil = tweetUtil;
        this.tweetMutedUserRepo = tweetMutedUserRepo;
        this.notificationRepo = notificationRepo;
        this.messagingService = messagingService;
    }

    public User getUserById(Long userId){
        return userRepo.findById(userId).orElseThrow(
                () -> new UserNotFoundException("user with id of "+ userId +" was not found!")
        );
    }

    public List<User> getUsers(String username){
        List<User> users = userRepo.findAll();
        List<Follow> followers = followRepo.findAll();
        for (User user: users){
            user.setTimeJoined(tweetUtil.tweetTime(user.getCreatedAt().toString()));
            user.setUserFollowed(user.getFollowList().stream().anyMatch( flw -> username.equals(flw.getUsername())));
            user.setHasProfilePicture(user.getProfilePicture() != null);
            messagingService.messageFunction(user.getMessageList());
            for (Follow follow: followers){
                if (follow.getUsername().equals(user.getUsername())){
                    user.setNumFollowers(user.getNumFollowers() + 1);
                }
            }
            for (Follow follower: user.getFollowList()){
                if (username.equals(follower.getUsername())){
                    user.getArrayOfFollowers().add(follower.getUsername());
                }
            }
        }

        return users;
    }

    public void updateUser(MultipartFile profilePicture, Long id, String firstName, String lastName, String bio,
                           String location, String birthDate){
        User userToUpdate = userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException("user with id of "+id+" was not found!")
        );

        if (profilePicture != null){
            try {
                userToUpdate.setProfilePicture(Base64.getEncoder().encodeToString(profilePicture.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        userToUpdate.setFirstName(firstName);
        userToUpdate.setLastName(lastName);
        userToUpdate.setBio(bio);
        userToUpdate.setLocation(location);
        userToUpdate.setBirthDate(birthDate);

        userRepo.save(userToUpdate);
    }

    public void followUser(String username, String usernameOfUserToFollow){

        User user = userRepo.findByUsername(username);
        User userTo = userRepo.findByUsername(usernameOfUserToFollow);
        Notifications notifications = new Notifications();
        notifications.setNotificationFrom(username);
        notifications.setUsername(usernameOfUserToFollow);
        notifications.setForUser(true);
        notifications.setForComment(false);
        notifications.setForTweet(false);
        notifications.setForCommentReply(false);

        if (!username.equals(usernameOfUserToFollow)) {
            Follow userToFollow = new Follow(usernameOfUserToFollow, user);

            if (!user.getFollowList().isEmpty()){
                boolean isUserFound = user.getFollowList().stream()
                        .anyMatch(follow -> usernameOfUserToFollow.equals(follow.getUsername()));

                if (isUserFound){
                    Follow follow = user.getFollowList().stream()
                            .filter(flw -> usernameOfUserToFollow.equals(flw.getUsername()))
                            .findFirst()
                            .orElseThrow(() -> new UserNotFollowException("this user "+ usernameOfUserToFollow+" is not following you!"));

                    user.getFollowList().remove(follow);
                    userRepo.save(user);
                    notifications.setContent(user.getFirstName()+" "+user.getLastName()+" unfollowed you.");
                    notificationRepo.save(notifications);
                }else{
                    user.getFollowList().add(userToFollow);
                    followRepo.save(userToFollow);
                    notifications.setContent(user.getFirstName()+" "+user.getLastName()+" followed you.");
                    notificationRepo.save(notifications);
                }
            }else {
                user.getFollowList().add(userToFollow);
                followRepo.save(userToFollow);
                notifications.setContent(user.getFirstName()+" "+user.getLastName()+" followed you.");
                notificationRepo.save(notifications);
            }

        }
    }

    public void blockUser(String username, String usernameOfUserToBlock){

        if (!username.equals(usernameOfUserToBlock)){

            User user = userRepo.findByUsername(username);
            BlockedUsers userToBlock = new BlockedUsers(usernameOfUserToBlock, user);

            if (!user.getBlockedUsersList().isEmpty()){

                boolean isUSerFound = user.getBlockedUsersList().stream()
                        .anyMatch(block -> usernameOfUserToBlock.equals(block.getUsername()));

                if (isUSerFound){
                    BlockedUsers blockedUser = user.getBlockedUsersList().stream()
                            .filter(block -> usernameOfUserToBlock.equals(block.getUsername()))
                            .findFirst()
                            .orElseThrow(
                                    () -> new UserNotFoundException("user was not found in blocked users list!")
                            );
                    user.getBlockedUsersList().remove(blockedUser);
                    userRepo.save(user);
                }else {
                    user.getBlockedUsersList().add(userToBlock);
                    userRepo.save(user);
                }
            }else {
                user.getBlockedUsersList().add(userToBlock);
                userRepo.save(user);
            }
        }
    }

    public void muteUser(String username, String usernameOfUserToMute){

        if (!username.equals(usernameOfUserToMute)){

            User user = userRepo.findByUsername(username);
            TweetMuteUser tweetMuteUser = new TweetMuteUser(usernameOfUserToMute, user);

            if(!user.getTweetMuteUserList().isEmpty()){

                boolean isUserFound = user.getTweetMuteUserList().stream()
                        .anyMatch(mute -> usernameOfUserToMute.equals(mute.getUsernameToMute()));

                if (isUserFound){
                    TweetMuteUser mutedUser = user.getTweetMuteUserList().stream()
                            .filter(mute -> usernameOfUserToMute.equals(mute.getUsernameToMute()))
                            .findFirst()
                            .orElseThrow(
                                    () -> new UserNotFoundException("user was not found in the mute list!")
                            );

                    user.getTweetMuteUserList().remove(mutedUser);
                    userRepo.save(user);
                }else {
                    user.getTweetMuteUserList().add(tweetMuteUser);
                    userRepo.save(user);
                }
            }else {
                user.getTweetMuteUserList().add(tweetMuteUser);
                userRepo.save(user);
            }


        }
    }

}
