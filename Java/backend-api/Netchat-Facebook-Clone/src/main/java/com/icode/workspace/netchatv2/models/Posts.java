package com.icode.workspace.netchatv2.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Post")
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String body;
    @Column(name = "added_by")
    private String addedBy;
    @Column(name = "user_to")
    private String userTo;
    @Column(name = "time_added")
    private String timeAdded;
    @Column(name = "user_closed")
    private String userClosed;
    private String deleted;
    private Integer likes;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String img;
    private String videoUrl;
    @Transient
    private String time;
    @Transient
    private int numberOfComments;
    @Transient
    private boolean userLiked;
    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<PostLikedBy> postLikedBy;
    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<Comments> comments;


    public Posts(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUserClosed() {
        return userClosed;
    }

    public void setUserClosed(String userClosed) {
        this.userClosed = userClosed;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public boolean getUserLiked() {
        return userLiked;
    }

    public void setUserLiked(boolean userLiked) {
        this.userLiked = userLiked;
    }

    public List<PostLikedBy> getPostLikedBy() {
        return postLikedBy;
    }

    public void setPostLikedBy(List<PostLikedBy> postLikedBy) {
        this.postLikedBy = postLikedBy;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public void addComment(Comments comment){

        if (comments == null){
            comments = new ArrayList<>();
        }

        comments.add(comment);

        comment.setPost(this);
    }

    public void addPostLikedBy(PostLikedBy likedBy){

        if (postLikedBy == null){
            postLikedBy = new ArrayList<>();
        }

        postLikedBy.add(likedBy);

        likedBy.setPost(this);
    }

    @Override
    public String toString() {
        return "Posts{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", addedBy='" + addedBy + '\'' +
                ", userTo='" + userTo + '\'' +
                ", timeAdded='" + timeAdded + '\'' +
                ", userClosed='" + userClosed + '\'' +
                ", deleted='" + deleted + '\'' +
                ", likes=" + likes +
                ", img='" + img + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", time='" + time + '\'' +
                ", numberOfComments=" + numberOfComments +
                ", isUserLiked=" + userLiked +
                ", postLikedBy=" + postLikedBy +
                ", comments=" + comments +
                '}';
    }
}
