package com.icode.workspace.netchatv2.models;

import javax.persistence.*;

@Entity
@Table(name = "friend")
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "friend_username")
    private String friendUsername;
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "user_id")
    private Users user;

    public Friends(){}

    public Friends(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public Friends(String friendUsername, Users user) {
        this.friendUsername = friendUsername;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Friends{" +
                "id=" + id +
                ", friendUsername='" + friendUsername + '\'' +
                ", user=" + user +
                '}';
    }
}
