package com.icode.workspace.netchatv2.models;

import javax.persistence.*;

@Entity
@Table(name = "CommentLikedBy")
public class CommentLikedBy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "liked_by")
    private String likedBy;
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "comment_id")
    private Comments comment;


    public CommentLikedBy(){}

    public CommentLikedBy(String likedBy, Comments comment) {
        this.likedBy = likedBy;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(String likedBy) {
        this.likedBy = likedBy;
    }

    public Comments getComment() {
        return comment;
    }

    public void setComment(Comments comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CommentLikedBy{" +
                "id=" + id +
                ", likedBy='" + likedBy + '\'' +
                ", comment=" + comment +
                '}';
    }
}
