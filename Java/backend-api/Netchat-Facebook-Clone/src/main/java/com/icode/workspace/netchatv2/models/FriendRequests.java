package com.icode.workspace.netchatv2.models;

import javax.persistence.*;

@Entity
public class FriendRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_to")
    private String userTo;
    @Column(name = "user_from")
    private String userFrom;

    public FriendRequests(){}

    public FriendRequests(String userTo, String userFrom) {
        this.userTo = userTo;
        this.userFrom = userFrom;
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

    @Override
    public String toString() {
        return "FriendRequests{" +
                "id=" + id +
                ", userTo='" + userTo + '\'' +
                ", userFrom='" + userFrom + '\'' +
                '}';
    }
}
