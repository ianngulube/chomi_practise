/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.entity.Request;
import com.ian.service.RequestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private MongoTemplate requestMongoTemplate;

    @Override
    public void saveRequest(Request request) {
        if (!requestMongoTemplate.collectionExists(Request.class)) {
            requestMongoTemplate.createCollection(Request.class);
        }
        requestMongoTemplate.insert(request, "Request");
    }

    @Override
    public void updateRequest(Request newRequest) {
        Request request = requestMongoTemplate.findOne(Query.query(Criteria.where("id").is(newRequest.getId())), Request.class, "Request");
        request.setDestination(newRequest.getDestination());
        request.setOrigin(newRequest.getOrigin());
        request.setTime(newRequest.getTime());
        request.setStatus(newRequest.getStatus());
        requestMongoTemplate.save(request, "Request");
    }

    @Override
    public List<Request> findAllRequestsByDestination(String destination) {
        return requestMongoTemplate.find(Query.query(Criteria.where("status").is("NEW")).addCriteria(Criteria.where("destination").is(destination)), Request.class, "Request");
    }

    @Override
    public Request findRequestById(String id) {
        return requestMongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Request.class, "Request");
    }

    @Override
    public void deleteRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Request findRequestByOrigin(String id) {
        return requestMongoTemplate.findOne(Query.query(Criteria.where("origin").is(id)), Request.class, "Request");
    }

    @Override
    public Request findNewRequestByOrigin(String id) {
        return requestMongoTemplate.findOne(Query.query(Criteria.where("origin").is(id)).addCriteria(Criteria.where("status").is("NEW")), Request.class, "Request");
    }

}
