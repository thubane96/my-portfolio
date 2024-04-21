package com.icode.workspace.netchatv2.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "post_id")
    private Long postId;
    @Column(name = "comment_body")
    private String commentBody;
    @Column(name = "comment_by")
    private String commentedBy;
    @Column(name = "comment_to")
    private String commentedTo;
    @Column(name = "time_commented")
    private String timeCommented;
    @Transient
    private String time;
    private int numOfLikes;
    private String removed;
    @Transient
    private boolean userLiked;
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "post_commented_id")
    private Posts post;
    @OneToMany(mappedBy = "comment", orphanRemoval = true,  cascade = CascadeType.ALL)
    private List<CommentLikedBy> commentLikedBy;

    public Comments(){}

    public Comments(Long postId, String commentBody, String commentedBy, String commentedTo,
                    String timeCommented, int numOfLikes, String removed, Posts post) {
        this.postId = postId;
        this.commentBody = commentBody;
        this.commentedBy = commentedBy;
        this.commentedTo = commentedTo;
        this.timeCommented = timeCommented;
        this.numOfLikes = numOfLikes;
        this.removed = removed;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }

    public String getCommentedTo() {
        return commentedTo;
    }

    public void setCommentedTo(String commentedTo) {
        this.commentedTo = commentedTo;
    }

    public String getTimeCommented() {
        return timeCommented;
    }

    public void setTimeCommented(String timeCommented) {
        this.timeCommented = timeCommented;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public String getRemoved() {
        return removed;
    }

    public void setRemoved(String removed) {
        this.removed = removed;
    }

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

    public boolean getUserLiked() {
        return userLiked;
    }

    public void setUserLiked(boolean userLiked) {
        this.userLiked = userLiked;
    }

    public List<CommentLikedBy> getCommentLikedBy() {
        return commentLikedBy;
    }

    public void setCommentLikedBy(List<CommentLikedBy> commentLikedBy) {
        this.commentLikedBy = commentLikedBy;
    }

    public void addCommentLikedBy(CommentLikedBy likedBy){

        if (commentLikedBy == null){
            commentLikedBy = new ArrayList<>();
        }

        commentLikedBy.add(likedBy);

        likedBy.setComment(this);
    }

    @Override
    public String toString() {
        return "Comments{" +
                "id=" + id +
                ", postId=" + postId +
                ", commentBody='" + commentBody + '\'' +
                ", commentedBy='" + commentedBy + '\'' +
                ", commentedTo='" + commentedTo + '\'' +
                ", timeCommented='" + timeCommented + '\'' +
                ", time='" + time + '\'' +
                ", numOfLikes=" + numOfLikes +
                ", removed='" + removed + '\'' +
                ", userLiked=" + userLiked +
                ", post=" + post +
                ", commentLikedBy=" + commentLikedBy +
                '}';
    }
}
