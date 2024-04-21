package com.icode.workspace.netchatv2.dto;

import org.springframework.web.multipart.MultipartFile;

public class PostDto {

    private Long userProfileId;
    private String postBody;
    private MultipartFile img;

    public PostDto(){}

    public PostDto(String postBody, MultipartFile img){
        this.postBody = postBody;
        this.img = img;
    }

    public PostDto(Long userProfileId, String postBody, MultipartFile img) {
        this.userProfileId = userProfileId;
        this.postBody = postBody;
        this.img = img;
    }

    public Long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public MultipartFile getImg() {
        return img;
    }

    public void setImg(MultipartFile img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "PostProfileDto{" +
                "userProfileId=" + userProfileId +
                ", postBody='" + postBody + '\'' +
                ", img=" + img +
                '}';
    }
}
