package com.icode.workspace.netchatv2.dto;

public class RejectFriendRequestDto {

    private Long friendRequestIdToDelete;

    public RejectFriendRequestDto(){};

    public RejectFriendRequestDto(Long friendRequestIdToDelete) {
        this.friendRequestIdToDelete = friendRequestIdToDelete;
    }

    public Long getFriendRequestIdToDelete() {
        return friendRequestIdToDelete;
    }

    public void setFriendRequestIdToDelete(Long friendRequestIdToDelete) {
        this.friendRequestIdToDelete = friendRequestIdToDelete;
    }

    @Override
    public String toString() {
        return "RejectFriendRequestDto{" +
                "friendRequestIdToDelete=" + friendRequestIdToDelete +
                '}';
    }
}
