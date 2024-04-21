package com.icode.workspace.netchatv2.models;

import javax.persistence.*;

@Entity
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_to")
    private String userTo;
    @Column(name = "user_from")
    private String userFrom;
    private String message;
    private Long postId;
    @Column(name = "time_added")
    private String timeAdded;
    @Transient
    private String time;
    private String opened;


    public Notifications(){}

    public Notifications(String userTo, String userFrom, String message, Long postId, String date,
                         String opened) {
        this.userTo = userTo;
        this.userFrom = userFrom;
        this.message = message;
        this.postId = postId;
        this.timeAdded = date;
        this.opened = opened;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "id=" + id +
                ", userTo='" + userTo + '\'' +
                ", userFrom='" + userFrom + '\'' +
                ", message='" + message + '\'' +
                ", link='" + postId + '\'' +
                ", date='" + timeAdded + '\'' +
                ", time='" + time + '\'' +
                ", opened='" + opened + '\'' +
                '}';
    }
}
