package com.icode.workspace.netchatv2.dto;

public class CommentDto {

    private String commentBody;
    private Long idReceived;

    public CommentDto(){}

    public CommentDto(String commentBody, Long idReceived) {
        this.commentBody = commentBody;
        this.idReceived = idReceived;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Long getIdReceived() {
        return idReceived;
    }

    public void setIdReceived(Long idReceived) {
        this.idReceived = idReceived;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "commentBody='" + commentBody + '\'' +
                ", idReceived=" + idReceived +
                '}';
    }
}
