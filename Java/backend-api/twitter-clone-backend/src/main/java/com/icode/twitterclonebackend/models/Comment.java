package com.icode.twitterclonebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "comment_body")
    private String commentBody;
    @Column(name = "comment_image", columnDefinition = "MEDIUMBLOB")
    private String commentImage;
    @Column(name = "comment_by")
    private String commentedBy;
    @Column(name = "comment_likes")
    private int commentLikes;
    @Transient
    private int numberOfCommentReplies;
    @Transient
    private String time;
    @Transient
    private Boolean isUserLiked;
    @Transient
    private boolean commentHasImage;
    @Transient
    private boolean userFollowed;
    @Transient
    private boolean userMuted;
    @Transient
    private boolean userBlocked;
    @OneToMany(mappedBy = "comment", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<CommentLikedBy> commentLikedByList = new ArrayList<>();
    @JsonIgnore
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "tweet_id")
    private Tweet tweet;
    @OneToMany(mappedBy = "comment", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<CommentReply> commentReplyList = new ArrayList<>();
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Comment(){}

    public Comment(String commentBody, String commentedBy, int commentLikes, Tweet tweet) {
        this.commentBody = commentBody;
        this.commentedBy = commentedBy;
        this.commentLikes = commentLikes;
        this.tweet = tweet;
    }

    public Comment(String commentBody, String commentImage, String commentedBy, int commentLikes, Tweet tweet) {
        this.commentBody = commentBody;
        this.commentImage = commentImage;
        this.commentedBy = commentedBy;
        this.commentLikes = commentLikes;
        this.tweet = tweet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public String getCommentImage() {
        return commentImage;
    }

    public void setCommentImage(String commentImage) {
        this.commentImage = commentImage;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }

    public int getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(int commentLikes) {
        this.commentLikes = commentLikes;
    }

    public int getNumberOfCommentReplies() {
        return numberOfCommentReplies;
    }

    public void setNumberOfCommentReplies(int numberOfCommentReplies) {
        this.numberOfCommentReplies = numberOfCommentReplies;
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

    public void setIsUserLiked(Boolean userLiked) {
        isUserLiked = userLiked;
    }

    public Boolean isCommentHasImage(){
        return commentHasImage;
    }

    public void setCommentHasImage(boolean commentHasImage) {
        this.commentHasImage = commentHasImage;
    }

    public Boolean getUserLiked() {
        return isUserLiked;
    }

    public void setUserLiked(Boolean userLiked) {
        isUserLiked = userLiked;
    }

    public boolean isUserFollowed() {
        return userFollowed;
    }

    public void setUserFollowed(boolean userFollowed) {
        this.userFollowed = userFollowed;
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

    public List<CommentLikedBy> getCommentLikedByList() {
        return commentLikedByList;
    }

    public void setCommentLikedByList(List<CommentLikedBy> commentLikedByList) {
        this.commentLikedByList = commentLikedByList;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public List<CommentReply> getCommentReplyList() {
        return commentReplyList;
    }

    public void setCommentReplyList(List<CommentReply> commentReplyList) {
        this.commentReplyList = commentReplyList;
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

    public void addCommentReply(CommentReply commentReply){
        if (commentReplyList == null){
            commentReplyList = new ArrayList<>();
        }

        commentReplyList.add(commentReply);
        commentReply.setComment(this);
    }

    public void addCommentLikedBy(CommentLikedBy likedBy){

        if (commentLikedByList == null){
            commentLikedByList = new ArrayList<>();
        }

        commentLikedByList.add(likedBy);
        likedBy.setComment(this);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", commentBody='" + commentBody + '\'' +
                ", commentImage='" + commentImage + '\'' +
                ", commentedBy='" + commentedBy + '\'' +
                ", commentLikes=" + commentLikes +
                ", numberOfCommentReplies=" + numberOfCommentReplies +
                ", time='" + time + '\'' +
                ", isUserLiked=" + isUserLiked +
                ", commentHasImage=" + commentHasImage +
                ", userFollowed=" + userFollowed +
                ", userMuted=" + userMuted +
                ", userBlocked=" + userBlocked +
                ", commentLikedByList=" + commentLikedByList +
                ", tweet=" + tweet +
                ", commentReplyList=" + commentReplyList +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
