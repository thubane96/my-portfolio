package com.icode.workspace.netchatv2.services;

import com.icode.workspace.netchatv2.apptools.ApplicationTools;
import com.icode.workspace.netchatv2.exception.RequestNotFoundException;
import com.icode.workspace.netchatv2.models.FriendRequests;
import com.icode.workspace.netchatv2.models.Friends;
import com.icode.workspace.netchatv2.models.Users;
import com.icode.workspace.netchatv2.repositories.FriendRepository;
import com.icode.workspace.netchatv2.repositories.FriendRequestRepository;
import com.icode.workspace.netchatv2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendService {

    private FriendRepository friendRepo;
    private FriendRequestRepository friendRequestRepo;
    private UserRepository userRepo;
    private ApplicationTools tools;

    private UserServiceImpl userService;

    @Autowired
    public FriendService(FriendRepository friendRepo, FriendRequestRepository friendRequestRepo,
                         UserRepository userRepo, UserServiceImpl userService, ApplicationTools tools){
        this.friendRepo = friendRepo;
        this.friendRequestRepo = friendRequestRepo;
        this.userRepo = userRepo;
        this.userService = userService;
        this.tools = tools;
    }

    public void addFriendRequest(Long sendToUserId, String username){
        friendRequestRepo.save(
                new FriendRequests(userService.getUsersDetails(sendToUserId).getUsername(), username)
        );
    }

    public void addFriend(Long requestId){
        FriendRequests request = friendRequestRepo.findById(requestId).orElseThrow(
                () -> new RequestNotFoundException("Friend request with id of "+ requestId +" was not found")
        );

        Users userFrom = userRepo.findByUsername(request.getUserFrom());
        Users userTo = userRepo.findByUsername(request.getUserTo());

        Friends friendTo = new Friends(userFrom.getUsername());
        Friends friendFrom = new Friends(userTo.getUsername());

        userFrom.add(friendFrom);
        userTo.add(friendTo);

        friendRepo.save(friendTo);
        friendRepo.save(friendFrom);

        friendRequestRepo.deleteById(requestId);
    }

    public FriendRequests friendRequestsDetails(String username){
        return friendRequestRepo.findByUserTo(username);
    }

    public boolean isRequestSent(Long userId, String username){
        List<FriendRequests> getRequests = friendRequestRepo.findAllByUserFrom(username);
        String name = userService.getUsersDetails(userId).getUsername();
        return getRequests.stream().anyMatch(requestsCheck -> name.equals(requestsCheck.getUserTo()));

    }

    public void cancelRequest(Long userId, String username){
        List<FriendRequests> getRequests = friendRequestRepo.findAllByUserFrom(username);
        String name = userService.getUsersDetails(userId).getUsername();

        FriendRequests friendRequest = getRequests.stream()
                .filter(request -> name.equals(request.getUserTo()))
                .findFirst()
                .orElseThrow(() -> new RequestNotFoundException("Request not found"));

        friendRequestRepo.deleteById(friendRequest.getId());
    }

    public List<FriendRequests> getFriendRequests(String username){
        return friendRequestRepo.findAllByUserTo(username);
    }

    public void rejectFriendRequest(Long friendRequestId){
        friendRequestRepo.deleteById(friendRequestId);
    }

    public void removeFriend(String username, Long friendId){
        Users user = userRepo.findByUsername(username);
        Users friendToRemove = userService.getUsersDetails(friendId);

        for (Friends friend: user.getFriends()){
            if (friend.getFriendUsername().equals(friendToRemove.getUsername())){
                user.getFriends().remove(friend);
                userRepo.save(user);
                break;
            }
        }
        for (Friends friend: friendToRemove.getFriends()){
            if (friend.getFriendUsername().equals(username)){
                friendToRemove.getFriends().remove(friend);
                userRepo.save(friendToRemove);
                break;
            }
        }

    }

    public List<Users> getSearch(String keywords){
        List<Users> foundUsers = new ArrayList<Users>();

        foundUsers.clear();
        String fullNames = null;
        for (Users user: userService.getAllUsers()){
            if (user.getUsername().toLowerCase().equals(keywords.toLowerCase())){
                foundUsers.add(user);
            }
            if (user.getFirstName().toLowerCase().equals(keywords.toLowerCase())){
                foundUsers.add(user);
            }
            if (user.getLastName().toLowerCase().equals(keywords.toLowerCase())){
                foundUsers.add(user);
            }
            fullNames = user.getFirstName()+" "+user.getLastName();
            if (fullNames.toLowerCase().equals(keywords.toLowerCase())){
                foundUsers.add(user);
            }
        }
        return foundUsers;
    }

    public boolean isFriend(Users user, String friendUsername){
        return user.getFriends().stream().anyMatch(friend -> friendUsername.equals(friend.getFriendUsername()));
    }

}
