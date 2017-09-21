/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.entity;

import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;

public class Trending {

    @Id
    private String id;
    private String title;
    private List<String> postIds;
    private Date time;
    private String postedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPostIds() {
        return postIds;
    }

    public void setPostIds(List<String> postIds) {
        this.postIds = postIds;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
