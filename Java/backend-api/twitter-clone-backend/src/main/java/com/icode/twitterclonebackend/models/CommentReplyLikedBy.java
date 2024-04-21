package com.icode.twitterclonebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CommentReplyLikedBy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String likedBy;
    @JsonIgnore
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "comment_reply_id")
    private CommentReply commentReply;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CommentReplyLikedBy(){}

    public CommentReplyLikedBy(String likedBy, CommentReply commentReply) {
        this.likedBy = likedBy;
        this.commentReply = commentReply;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(String likedBy) {
        this.likedBy = likedBy;
    }

    public CommentReply getCommentReply() {
        return commentReply;
    }

    public void setCommentReply(CommentReply commentReply) {
        this.commentReply = commentReply;
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
    public String toString() {
        return "CommentReplyLikedBy{" +
                "id=" + id +
                ", likedBy='" + likedBy + '\'' +
                ", commentReply=" + commentReply +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
