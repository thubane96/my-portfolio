package com.icode.twitterclonebackend.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "profile_picture", columnDefinition = "MEDIUMBLOB")
    private String profilePicture;
    @Column(name = "bio")
    private String bio;
    @Column(name = "birth_date")
    private String birthDate;
    private String location;
    @Transient
    private String timeJoined;
    @Transient
    private int numFollowers;
    @Transient
    private boolean userFollowed;
    @Transient
    private boolean hasProfilePicture;
    @Transient
    private List<String> arrayOfFollowers = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<NotInterestedTweet> notInterestedTweetList = new ArrayList<>();
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<TweetMuteUser> tweetMuteUserList = new ArrayList<>();
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<BlockedUsers> blockedUsersList = new ArrayList<>();
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<Follow> followList = new ArrayList<>();
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "pinned_tweet_id", referencedColumnName = "id")
    private PinnedTweet pinnedTweet;
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<TweetList> tweetList = new ArrayList<>();
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<Bookmarks> bookmarkList = new ArrayList<>();
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<Message> messageList = new ArrayList<>();
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<HiddenTweets> hiddenTweetsList = new ArrayList<>();
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public User(){}

    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public User(String firstName, String lastName, String username, String password, Collection<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeJoined() {
        return timeJoined;
    }

    public void setTimeJoined(String timeJoined) {
        this.timeJoined = timeJoined;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public void setNumFollowers(int numFollowers) {
        this.numFollowers = numFollowers;
    }

    public boolean isUserFollowed() {
        return userFollowed;
    }

    public void setUserFollowed(boolean userFollowed) {
        this.userFollowed = userFollowed;
    }

    public boolean isHasProfilePicture() {
        return hasProfilePicture;
    }

    public void setHasProfilePicture(boolean hasProfilePicture) {
        this.hasProfilePicture = hasProfilePicture;
    }

    public List<String> getArrayOfFollowers() {
        return arrayOfFollowers;
    }

    public void setArrayOfFollowers(List<String> arrayOfFollowers) {
        this.arrayOfFollowers = arrayOfFollowers;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public List<NotInterestedTweet> getNotInterestedTweetList() {
        return notInterestedTweetList;
    }

    public void setNotInterestedTweetList(List<NotInterestedTweet> notInterestedTweetList) {
        this.notInterestedTweetList = notInterestedTweetList;
    }

    public List<TweetMuteUser> getTweetMuteUserList() {
        return tweetMuteUserList;
    }

    public void setTweetMuteUserList(List<TweetMuteUser> tweetMuteUserList) {
        this.tweetMuteUserList = tweetMuteUserList;
    }

    public List<BlockedUsers> getBlockedUsersList() {
        return blockedUsersList;
    }

    public void setBlockedUsersList(List<BlockedUsers> blockedUsersList) {
        this.blockedUsersList = blockedUsersList;
    }


    public List<Follow> getFollowList() {
        return followList;
    }

    public void setFollowList(List<Follow> followList) {
        this.followList = followList;
    }

    public PinnedTweet getPinnedTweet() {
        return pinnedTweet;
    }

    public void setPinnedTweet(PinnedTweet pinnedTweet) {
        this.pinnedTweet = pinnedTweet;
    }

    public List<TweetList> getTweetList() {
        return tweetList;
    }

    public void setTweetList(List<TweetList> tweetList) {
        this.tweetList = tweetList;
    }

    public List<Bookmarks> getBookmarkList() {
        return bookmarkList;
    }

    public void setBookmarkList(List<Bookmarks> bookmarkList) {
        this.bookmarkList = bookmarkList;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<HiddenTweets> getHiddenTweetsList() {
        return hiddenTweetsList;
    }

    public void setHiddenTweetsList(List<HiddenTweets> hiddenTweetsList) {
        this.hiddenTweetsList = hiddenTweetsList;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addNotInterestedTweet(NotInterestedTweet notInterestedTweet){

        if (notInterestedTweetList == null){
            notInterestedTweetList = new ArrayList<>();
        }

        notInterestedTweetList.add(notInterestedTweet);
        notInterestedTweet.setUser(this);
    }

    public void addTweetToBeMuted(TweetMuteUser tweetUserToMute){

        if (tweetMuteUserList == null){
            tweetMuteUserList = new ArrayList<>();
        }

        tweetMuteUserList.add(tweetUserToMute);
        tweetUserToMute.setUser(this);
    }

    public void addUserToBlock(BlockedUsers blockedUser){

        if (blockedUsersList == null){
            blockedUsersList = new ArrayList<>();
        }

        blockedUsersList.add(blockedUser);
        blockedUser.setUser(this);
    }

    public void addUserToFollow(Follow userToFollow){

        if (followList == null){
            followList = new ArrayList<>();
        }

        followList.add(userToFollow);
        userToFollow.setUser(this);
    }

    public void addBookmark(Bookmarks bookmark){

        if (bookmarkList == null){
            bookmarkList = new ArrayList<>();
        }

        bookmarkList.add(bookmark);
        bookmark.setUser(this);
    }

    public void addTweetToList(TweetList tweet){

        if (tweetList == null){
            tweetList = new ArrayList<>();
        }

        tweetList.add(tweet);
        tweet.setUser(this);
    }

    public void addToHiddenTweets(HiddenTweets tweet){

        if (hiddenTweetsList == null){
            hiddenTweetsList = new ArrayList<>();
        }

        hiddenTweetsList.add(tweet);
        tweet.setUser(this);
    }

    public void addMesssage(Message message){

        if (messageList == null){
            messageList = new ArrayList<>();
        }

        messageList.add(message);
        message.setUser(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", bio='" + bio + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", location='" + location + '\'' +
                ", timeJoined='" + timeJoined + '\'' +
                ", numFollowers=" + numFollowers +
                ", userFollowed=" + userFollowed +
                ", hasProfilePicture=" + hasProfilePicture +
                ", arrayOfFollowers=" + arrayOfFollowers +
                ", roles=" + roles +
                ", notInterestedTweetList=" + notInterestedTweetList +
                ", tweetMuteUserList=" + tweetMuteUserList +
                ", blockedUsersList=" + blockedUsersList +
                ", followList=" + followList +
                ", pinnedTweet=" + pinnedTweet +
                ", tweetList=" + tweetList +
                ", bookmarkList=" + bookmarkList +
                ", messageList=" + messageList +
                ", hiddenTweetsList=" + hiddenTweetsList +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
