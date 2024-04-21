package com.icode.twitterclonebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String friendMessaging;
    @OneToMany(mappedBy = "message", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<Messages> messagesList = new ArrayList<>();
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;
    @Transient
    private int numberOfUnopenedMessages;
    @Transient
    private String lastMessageContent;
    @Transient
    private String time;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Message(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFriendMessaging() {
        return friendMessaging;
    }

    public void setFriendMessaging(String friendMessaging) {
        this.friendMessaging = friendMessaging;
    }

    public List<Messages> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<Messages> messagesList) {
        this.messagesList = messagesList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNumberOfUnopenedMessages() {
        return numberOfUnopenedMessages;
    }

    public void setNumberOfUnopenedMessages(int numberOfUnopenedMessages) {
        this.numberOfUnopenedMessages = numberOfUnopenedMessages;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
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

    public void addMessage(Messages message){

        if (messagesList == null){
            messagesList = new ArrayList<>();
        }

        messagesList.add(message);
        message.setMessage(this);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", friendMessaging='" + friendMessaging + '\'' +
                ", messagesList=" + messagesList +
                ", user=" + user +
                ", numberOfUnopenedMessages=" + numberOfUnopenedMessages +
                ", lastMessageContent='" + lastMessageContent + '\'' +
                ", time='" + time + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
