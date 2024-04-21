package com.icode.twitterclonebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commentReply")
public class CommentReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "comment_reply_body")
    private String commentReplyBody;
    @Column(name = "comment_reply_image", columnDefinition = "MEDIUMBLOB")
    private String commentReplyImage;
    @Column(name = "comment_replied_by")
    private String commentRepliedBy;
    @Column(name = "comment_reply_likes")
    private int commentReplyLikes;
    @Transient
    private String time;
    @Transient
    private Boolean isUserLiked;
    @Transient
    private boolean commentReplyHasImage;
    @Transient
    private boolean userFollowed;
    @Transient
    private boolean userMuted;
    @Transient
    private boolean userBlocked;
    @OneToMany(mappedBy = "commentReply", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<CommentReplyLikedBy> commentReplyLikedByList = new ArrayList<>();
    @JsonIgnore
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CommentReply(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentReplyBody() {
        return commentReplyBody;
    }

    public void setCommentReplyBody(String commentReplyBody) {
        this.commentReplyBody = commentReplyBody;
    }

    public String getCommentReplyImage() {
        return commentReplyImage;
    }

    public void setCommentReplyImage(String commentReplyImage) {
        this.commentReplyImage = commentReplyImage;
    }

    public String getCommentRepliedBy() {
        return commentRepliedBy;
    }

    public void setCommentRepliedBy(String commentRepliedBy) {
        this.commentRepliedBy = commentRepliedBy;
    }

    public int getCommentReplyLikes() {
        return commentReplyLikes;
    }

    public void setCommentReplyLikes(int commentReplyLikes) {
        this.commentReplyLikes = commentReplyLikes;
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

    public boolean isCommentReplyHasImage() {
        return commentReplyHasImage;
    }

    public void setCommentReplyHasImage(boolean commentReplyHasImage) {
        this.commentReplyHasImage = commentReplyHasImage;
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

    public List<CommentReplyLikedBy> getCommentReplyLikedByList() {
        return commentReplyLikedByList;
    }

    public void setCommentReplyLikedByList(List<CommentReplyLikedBy> commentReplyLikedByList) {
        this.commentReplyLikedByList = commentReplyLikedByList;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
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

    public void addCommentReplyLikedBy(CommentReplyLikedBy likedBy){

        if (commentReplyLikedByList == null){
            commentReplyLikedByList = new ArrayList<>();
        }

        commentReplyLikedByList.add(likedBy);
        likedBy.setCommentReply(this);
    }

    @Override
    public String toString() {
        return "CommentReply{" +
                "id=" + id +
                ", commentReplyBody='" + commentReplyBody + '\'' +
                ", commentReplyImage='" + commentReplyImage + '\'' +
                ", commentRepliedBy='" + commentRepliedBy + '\'' +
                ", commentReplyLikes=" + commentReplyLikes +
                ", time='" + time + '\'' +
                ", isUserLiked=" + isUserLiked +
                ", commentReplyHasImage=" + commentReplyHasImage +
                ", userFollowed=" + userFollowed +
                ", userMuted=" + userMuted +
                ", userBlocked=" + userBlocked +
                ", commentReplyLikedByList=" + commentReplyLikedByList +
                ", comment=" + comment +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
