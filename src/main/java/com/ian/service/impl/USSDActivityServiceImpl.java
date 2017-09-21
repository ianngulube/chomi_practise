/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.entity.USSDActivity;
import com.ian.service.USSDActivityService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class USSDActivityServiceImpl implements USSDActivityService {

    @Autowired
    private MongoTemplate uSSDActivityServiceMongoTemplate;

    @Override
    public void addUSSDActivity(USSDActivity _USSDActivity) {
        if (!uSSDActivityServiceMongoTemplate.collectionExists(USSDActivity.class)) {
            uSSDActivityServiceMongoTemplate.createCollection(USSDActivity.class);
        }
        uSSDActivityServiceMongoTemplate.insert(_USSDActivity, "USSDActivity");
    }

    @Override
    public void updateUSSDActivity(USSDActivity _USSDActivity) {
        USSDActivity uSSDActivity = uSSDActivityServiceMongoTemplate.findOne(Query.query(Criteria.where("id").is(_USSDActivity.getId())), USSDActivity.class, "USSDActivity");
        uSSDActivity.setAccessed(new Date());
        uSSDActivity.setMsisdn(_USSDActivity.getMsisdn());
        uSSDActivity.setUssdString(_USSDActivity.getUssdString());
        uSSDActivityServiceMongoTemplate.save(uSSDActivity, "USSDActivity");
    }

    @Override
    public List<USSDActivity> findAllUSSDActivities() {
        return uSSDActivityServiceMongoTemplate.findAll(USSDActivity.class, "USSDActivity");
    }

    @Override
    public void deleteUSSDActivity(String id) {
        try {
            uSSDActivityServiceMongoTemplate.remove(Query.query(Criteria.where("id").is(id)), USSDActivity.class, "USSDActivity");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
