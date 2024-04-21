package com.icode.journalbackend.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String title;
    private String body;
    @Column(name = "time_posted", nullable = false, updatable = false)
    private String timePosted;
    @Column(name = "date_posted", nullable = false, updatable = false)
    private String datePosted;
    @Column(insertable = false)
    private String time;

    public Post(){}

    public Post(String title, String body, String timePosted, String datePosted, String time) {
        this.title = title;
        this.body = body;
        this.timePosted = timePosted;
        this.datePosted = datePosted;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", timePosted='" + timePosted + '\'' +
                ", datePosted='" + datePosted + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
