package com.icode.twitterclonebackend.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tweet")
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tweet_body", length = 1000)
    private String tweetBody;
    @Column(name = "tweet_image", columnDefinition = "MEDIUMBLOB")
    private String tweetImage;
    @Column(name = "tweeted_by")
    private String tweetedBy;
    private int likes;
    @Transient
    private String time;
    @Transient
    private Boolean isUserLiked;
    @Transient
    private int numberOfComments;
    @Transient
    private boolean tweetHasImage;
    @Transient
    private boolean userMuted;
    @Transient
    private boolean userBlocked;
    @Transient
    private boolean tweetPinned;
    @Transient
    private boolean tweetBookmarked;
    @Transient
    private boolean userFollowed;
    @OneToMany(mappedBy = "tweet", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<TweetLikedBy> tweetLikedBy = new ArrayList<>();
    @OneToMany(mappedBy = "tweet", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<Comment> comments = new ArrayList<>();
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Tweet(){}

    public Tweet(String tweetBody, String tweetedBy, int likes) {
        this.tweetBody = tweetBody;
        this.tweetedBy = tweetedBy;
        this.likes = likes;
    }

    public Tweet(String tweetBody, String tweetImage, String tweetedBy, int likes) {
        this.tweetBody = tweetBody;
        this.tweetImage = tweetImage;
        this.tweetedBy = tweetedBy;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTweetBody() {
        return tweetBody;
    }

    public void setTweetBody(String tweetBody) {
        this.tweetBody = tweetBody;
    }

    public String getTweetImage() {
        return tweetImage;
    }

    public void setTweetImage(String tweetImage) {
        this.tweetImage = tweetImage;
    }

    public String getTweetedBy() {
        return tweetedBy;
    }

    public void setTweetedBy(String tweetedBy) {
        this.tweetedBy = tweetedBy;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getIsUserLiked() {
        return isUserLiked;
    }

    public void setIsUserLiked(Boolean isUserLiked) {
        this.isUserLiked = isUserLiked;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public boolean isTweetHasImage() {
        return tweetHasImage;
    }

    public void setTweetHasImage(boolean tweetHasImage) {
        this.tweetHasImage = tweetHasImage;
    }

    public Boolean getUserLiked() {
        return isUserLiked;
    }

    public void setUserLiked(Boolean userLiked) {
        isUserLiked = userLiked;
    }

    public boolean isUserMuted() {
        return userMuted;
    }

    public void setUserMuted(boolean userMuted) {
        this.userMuted = userMuted;
    }

    public boolean isUserBlocked() {
        return userBlocked;
    }

    public void setUserBlocked(boolean userBlocked) {
        this.userBlocked = userBlocked;
    }

    public boolean isTweetPinned() {
        return tweetPinned;
    }

    public void setTweetPinned(boolean tweetPinned) {
        this.tweetPinned = tweetPinned;
    }

    public boolean isTweetBookmarked() {
        return tweetBookmarked;
    }

    public void setTweetBookmarked(boolean tweetBookmarked) {
        this.tweetBookmarked = tweetBookmarked;
    }

    public boolean isUserFollowed() {
        return userFollowed;
    }

    public void setUserFollowed(boolean userFollowed) {
        this.userFollowed = userFollowed;
    }

    public List<TweetLikedBy> getTweetLikedBy() {
        return tweetLikedBy;
    }

    public void setTweetLikedBy(List<TweetLikedBy> tweetLikedBy) {
        this.tweetLikedBy = tweetLikedBy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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

    public void addTweetLikedBy(TweetLikedBy likedBy){

        if (tweetLikedBy == null){
            tweetLikedBy = new ArrayList<>();
        }

        tweetLikedBy.add(likedBy);
        likedBy.setTweet(this);
    }

    public void addComment(Comment comment){
        if (comments == null){
            comments = new ArrayList<>();
        }

        comments.add(comment);
        comment.setTweet(this);
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", tweetBody='" + tweetBody + '\'' +
                ", tweetImage='" + tweetImage + '\'' +
                ", tweetedBy='" + tweetedBy + '\'' +
                ", likes=" + likes +
                ", time='" + time + '\'' +
                ", isUserLiked=" + isUserLiked +
                ", numberOfComments=" + numberOfComments +
                ", tweetHasImage=" + tweetHasImage +
                ", userMuted=" + userMuted +
                ", userBlocked=" + userBlocked +
                ", tweetPinned=" + tweetPinned +
                ", tweetBookmarked=" + tweetBookmarked +
                ", userFollowed=" + userFollowed +
                ", tweetLikedBy=" + tweetLikedBy +
                ", comments=" + comments +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
