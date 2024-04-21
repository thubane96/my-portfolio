package com.icode.workspace.netchatv2.models;

import javax.persistence.*;

@Entity
@Table(name = "message")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_to")
    private  String userTo;
    @Column(name = "user_from")
    private String userFrom;
    private String body;
    @Column(name = "time_messaged")
    private String timeMessaged;
    private String opened;
    private String deleted;
    @Transient
    private String time;

    public Messages(){}

    public Messages(String userTo, String userFrom, String body, String timeMessaged,
                    String opened, String deleted) {
        this.userTo = userTo;
        this.userFrom = userFrom;
        this.body = body;
        this.timeMessaged = timeMessaged;
        this.opened = opened;
        this.deleted = deleted;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimeMessaged() {
        return timeMessaged;
    }

    public void setTimeMessaged(String timeMessaged) {
        this.timeMessaged = timeMessaged;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "id=" + id +
                ", userTo='" + userTo + '\'' +
                ", userFrom='" + userFrom + '\'' +
                ", body='" + body + '\'' +
                ", timeMessaged='" + timeMessaged + '\'' +
                ", opened='" + opened + '\'' +
                ", deleted='" + deleted + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
