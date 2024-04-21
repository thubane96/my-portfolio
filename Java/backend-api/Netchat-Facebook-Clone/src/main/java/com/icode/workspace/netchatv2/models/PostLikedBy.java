package com.icode.workspace.netchatv2.models;

import javax.persistence.*;

@Entity
@Table(name = "PostLikedBy")
public class PostLikedBy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "liked_by")
    private String likedBy;
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "post_id")
    private Posts post;
    public PostLikedBy() {
    }

    public PostLikedBy(String likedBy, Posts post) {
        this.likedBy = likedBy;
        this.post = post;
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

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "PostLikedBy{" +
                "id=" + id +
                ", likedBy='" + likedBy + '\'' +
                ", post=" + post +
                '}';
    }
}
