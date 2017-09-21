/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.entity;

import org.springframework.data.annotation.Id;

public class Language {

    @Id
    private String id;
    public String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
