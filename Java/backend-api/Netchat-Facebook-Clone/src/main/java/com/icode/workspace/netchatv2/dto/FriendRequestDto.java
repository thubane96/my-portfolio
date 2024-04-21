package com.icode.workspace.netchatv2.dto;

public class FriendRequestDto {

    private String userFrom;
    public Long userId;
    private Long friendRequestId;
    private Long paramId;

    public FriendRequestDto(){}

    public FriendRequestDto(String userFrom, Long userId, Long friendRequestId, Long paramId) {
        this.userFrom = userFrom;
        this.userId = userId;
        this.friendRequestId = friendRequestId;
        this.paramId = paramId;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriendRequestId() {
        return friendRequestId;
    }

    public void setFriendRequestId(Long friendRequestId) {
        this.friendRequestId = friendRequestId;
    }

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }

    @Override
    public String toString() {
        return "FriendRequestDto{" +
                "userFrom='" + userFrom + '\'' +
                ", userId=" + userId +
                ", friendRequestId=" + friendRequestId +
                ", paramId=" + paramId +
                '}';
    }
}
