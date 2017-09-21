/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.entity;

import org.springframework.data.annotation.Id;

public class Country {

    @Id
    private String id;
    public String name;
    public String countryId;
}
