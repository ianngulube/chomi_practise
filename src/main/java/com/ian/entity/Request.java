/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.entity;

import java.util.Date;
import org.springframework.data.annotation.Id;

public class Request {

    @Id
    private String id;
    public Date time;
    public String origin;
    public String destination;
    public String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
