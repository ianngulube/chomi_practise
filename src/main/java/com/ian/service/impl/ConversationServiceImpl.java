/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.entity.Conversation;
import com.ian.service.ConversationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private MongoTemplate conversationMongoTemplate;

    @Override
    public void addConversation(Conversation conversation) {
        if (!conversationMongoTemplate.collectionExists(Conversation.class)) {
            conversationMongoTemplate.createCollection(Conversation.class);
        }
        conversationMongoTemplate.insert(conversation, "Conversation");
    }

    @Override
    public void updateConversation(Conversation newConversation) {
        Conversation conversation = conversationMongoTemplate.findOne(Query.query(Criteria.where("id").is(newConversation.getId())), Conversation.class, "Conversation");
        conversation.setTime(newConversation.getTime());
        conversation.setUserOne(newConversation.getUserOne());
        conversation.setUserTwo(newConversation.getUserTwo());
        conversationMongoTemplate.save(conversation, "Conversation");
    }

    @Override
    public List<Conversation> findAllConversations() {
        return conversationMongoTemplate.findAll(Conversation.class, "Conversation");
    }

    @Override
    public void deleteConversation(String id) {
        conversationMongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Conversation.class, "Conversation");
    }

    @Override
    public Conversation findConversationById(String id) {
        return conversationMongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Conversation.class, "Conversation");
    }

    @Override
    public Conversation findConversationBetweenTwo(String userOne, String userTwo) {
        Conversation con = conversationMongoTemplate.findOne(Query.query(Criteria.where("userOne").is(userOne).and("userTwo").is(userTwo)), Conversation.class, "Conversation");
        if (con == null) {
            con = conversationMongoTemplate.findOne(Query.query(Criteria.where("userTwo").is(userOne).and("userOne").is(userTwo)), Conversation.class, "Conversation");
        }
        return con;
    }

    @Override
    public List<Conversation> findMyConversations(String personId) {
        List<Conversation> cons = conversationMongoTemplate.find(Query.query(Criteria.where("userOne").is(personId)), Conversation.class, "Conversation");
        List<Conversation> cons2 = conversationMongoTemplate.find(Query.query(Criteria.where("userTwo").is(personId)), Conversation.class, "Conversation");
        List<Conversation> all = new ArrayList<>();
        all.addAll(cons);
        all.addAll(cons2);
        return all;
    }

    @Override
    public List<Conversation> findConversations(List<String> ids) {
        List<Conversation> cr = new ArrayList<>();
        for (String id : ids) {
            cr.add(this.findConversationById(id));
        }
        return cr;
    }

}
