package com.ian.chomibridge;

import java.util.Date;

public class HashTag {

    private int hashTagId;
    private String topic;
    private Date dateCreated;
    private int createdByUserId;
    private int postId;

    public int getHashTagId() {
        return hashTagId;
    }

    public void setHashTagId(int hashTagId) {
        this.hashTagId = hashTagId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

}
