package com.icode.workspace.netchatv2.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String username;
    private String password;
    @Column(name = "signup_date")
    private String signupDate;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB", name = "profile_pic")
    private String profilePic;
    @Column(name = "user_closed")
    private String userClosed;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Roles> roles;
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH })
    private List<Friends> friends;

    public Users(){}

    public Users(String firstName, String lastName, String username, String password, String signupDate,
                 String userClosed, Collection<Roles> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.signupDate = signupDate;
        this.userClosed = userClosed;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(String signupDate) {
        this.signupDate = signupDate;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }



    public String getUserClosed() {
        return userClosed;
    }

    public void setUserClosed(String userClosed) {
        this.userClosed = userClosed;
    }


    public Collection<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Roles> roles) {
        this.roles = roles;
    }

    public List<Friends> getFriends() {
        return friends;
    }

    public void setFriends(List<Friends> friends) {
        this.friends = friends;
    }

    public void add(Friends friend){

        if (friends == null){
            friends = new ArrayList<>();
        }

        friends.add(friend);

        friend.setUser(this);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", signupDate='" + signupDate + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", userClosed='" + userClosed + '\'' +
                ", roles=" + roles +
                ", friends=" + friends +
                '}';
    }


}
