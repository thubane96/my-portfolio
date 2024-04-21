package com.icode.workspace.netchatv2.dto;

public class PostDeleteDto {

    private Long postId;
    private long userId;

    public PostDeleteDto(){}

    public PostDeleteDto(Long postId, long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PostDeleteDto{" +
                "postId=" + postId +
                ", userId=" + userId +
                '}';
    }
}
