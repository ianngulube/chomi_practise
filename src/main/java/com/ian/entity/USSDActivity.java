package com.ian.entity;

/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
import java.util.Date;
import org.springframework.data.annotation.Id;

public class USSDActivity {

    @Id
    private String id;
    public String msisdn;
    public Date accessed;
    public String ussdString;

    public String getId() {
        return id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Date getAccessed() {
        return accessed;
    }

    public void setAccessed(Date accessed) {
        this.accessed = accessed;
    }

    public String getUssdString() {
        return ussdString;
    }

    public void setUssdString(String ussdString) {
        this.ussdString = ussdString;
    }

}
