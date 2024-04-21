package com.icode.workspace.netchatv2.dto;

public class MessageDto {

    private Long userToId;
    private  String userTo;
    private String userFrom;
    private String body;

    public MessageDto(){}

    public MessageDto(String userTo, String userFrom, String body, Long userToId) {
        this.userTo = userTo;
        this.userFrom = userFrom;
        this.body = body;
        this.userToId = userToId;
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

    public Long getUserToId() {
        return userToId;
    }

    public void setUserToId(Long userToId) {
        this.userToId = userToId;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "userToId=" + userToId +
                ", userTo='" + userTo + '\'' +
                ", userFrom='" + userFrom + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
