/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.entity.USSDActivity;
import java.util.List;

public interface USSDActivityService {

    void addUSSDActivity(USSDActivity _USSDActivity);

    void updateUSSDActivity(USSDActivity _USSDActivity);

    List<USSDActivity> findAllUSSDActivities();

    void deleteUSSDActivity(String id);
}
