/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.entity.Message;
import com.ian.service.MessageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MongoTemplate messageMongoTemplate;

    @Override
    public void addMessage(Message message) {
        if (!messageMongoTemplate.collectionExists(Message.class)) {
            messageMongoTemplate.createCollection(Message.class);
        }
        messageMongoTemplate.insert(message, "Message");
    }

    @Override
    public void updateMessage(Message newMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Message> findAllMessages() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Message> findAllMessagesByMsisdn(String msisdn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteMessage(String id) {
        messageMongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Message.class, "Message");
    }

    @Override
    public List<Message> findAllMessagesByTo(String to) {
        return messageMongoTemplate.find(Query.query(Criteria.where("to").is(to)), Message.class, "Message");
    }

    @Override
    public Message findMessageById(String id) {
        return messageMongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Message.class, "Message");
    }

}
