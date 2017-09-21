/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.entity;

import java.util.Date;
import org.springframework.data.annotation.Id;

public class Customer {

    @Id
    private String id;
    public String name;
    public String address;
    private Date lastModified;

    public Customer(String name, String address) {
        super();
        this.name = name;
        this.address = address;
    }

    public Customer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "Customer [name=" + name + ", address=" + address + "]";
    }
}
