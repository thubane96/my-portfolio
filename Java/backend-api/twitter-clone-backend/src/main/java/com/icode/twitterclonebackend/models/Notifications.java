package com.icode.twitterclonebackend.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tweetId;
    private String username;
    private String notificationFrom;
    private Long commentId;
    private Long commentReplyId;
    private String content;
    private boolean forTweet;
    private boolean forUser;
    private boolean forComment;
    private boolean forCommentReply;
    private boolean opened;
    @Transient
    private String time;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Notifications(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNotificationFrom() {
        return notificationFrom;
    }

    public void setNotificationFrom(String notificationFrom) {
        this.notificationFrom = notificationFrom;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentReplyId() {
        return commentReplyId;
    }

    public void setCommentReplyId(Long commentReplyId) {
        this.commentReplyId = commentReplyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isForTweet() {
        return forTweet;
    }

    public void setForTweet(boolean forTweet) {
        this.forTweet = forTweet;
    }

    public boolean isForUser() {
        return forUser;
    }

    public void setForUser(boolean forUser) {
        this.forUser = forUser;
    }

    public boolean isForComment() {
        return forComment;
    }

    public void setForComment(boolean forComment) {
        this.forComment = forComment;
    }

    public boolean isForCommentReply() {
        return forCommentReply;
    }

    public void setForCommentReply(boolean forCommentReply) {
        this.forCommentReply = forCommentReply;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    @Override
    public String
    toString() {
        return "Notifications{" +
                "id=" + id +
                ", tweetId=" + tweetId +
                ", username='" + username + '\'' +
                ", notificationFrom='" + notificationFrom + '\'' +
                ", commentId=" + commentId +
                ", commentReplyId=" + commentReplyId +
                ", content='" + content + '\'' +
                ", forTweet=" + forTweet +
                ", forUser=" + forUser +
                ", forComment=" + forComment +
                ", forCommentReply=" + forCommentReply +
                ", opened=" + opened +
                ", time='" + time + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
