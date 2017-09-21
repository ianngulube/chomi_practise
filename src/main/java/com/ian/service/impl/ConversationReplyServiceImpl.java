package com.ian.service.impl;

import com.ian.entity.Conversation;
import com.ian.entity.ConversationReply;
import com.ian.service.ConversationReplyService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ConversationReplyServiceImpl implements ConversationReplyService {

    @Autowired
    private MongoTemplate conversationReplyMongoTemplate;

    @Override
    public void addConversationReply(ConversationReply conversationReply) {
        if (!conversationReplyMongoTemplate.collectionExists(ConversationReply.class)) {
            conversationReplyMongoTemplate.createCollection(ConversationReply.class);
        }
        conversationReplyMongoTemplate.insert(conversationReply, "ConversationReply");
    }

    @Override
    public void updateConversationReply(ConversationReply newConversationReply) {
        ConversationReply conversationReply = conversationReplyMongoTemplate.findOne(Query.query(Criteria.where("id").is(newConversationReply.getId())), ConversationReply.class, "ConversationReply");
        conversationReply.setConversationId(newConversationReply.getConversationId());
        conversationReply.setReply(newConversationReply.getReply());
        conversationReply.setTime(new Date());
        conversationReply.setUserId(newConversationReply.getUserId());
        conversationReplyMongoTemplate.save(conversationReply, "ConversationReply");
    }

    @Override
    public List<ConversationReply> findAllConversationReplies() {
        return conversationReplyMongoTemplate.findAll(ConversationReply.class, "ConversationReply");
    }

    @Override
    public void deleteConversationReply(String id) {
        conversationReplyMongoTemplate.remove(Query.query(Criteria.where("id").is(id)), ConversationReply.class, "ConversationReply");
    }

    @Override
    public ConversationReply findConversationReplyById(String id) {
        return conversationReplyMongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), ConversationReply.class, "ConversationReply");
    }

    @Override
    public List<ConversationReply> findAllConversationsByConversationId(String conversationId) {
        return conversationReplyMongoTemplate.find(Query.query(Criteria.where("conversationId").is(conversationId)), ConversationReply.class, "ConversationReply");
    }

    @Override
    public List<ConversationReply> findAllConversationRepliesSentByMe(String myId, List<Conversation> myConversations) {
        List<ConversationReply> crs = new ArrayList<>();
        List<ConversationReply> crs2 = new ArrayList<>();
        for (Conversation c : myConversations) {
            List<ConversationReply> cr = conversationReplyMongoTemplate.find(Query.query(Criteria.where("conversationId").is(c.getId())), ConversationReply.class, "ConversationReply");
            crs.addAll(cr);
        }
        System.out.println("Print the number of conversation replies i.e. number of messages, whether they be inbound or outbound. ~~~~~~~~~~~~~~~~~~~~~~" + crs.size());
        for (ConversationReply cr : crs) {
            System.out.println("CR ID~~~~~~~~~~~~~~~~~" + cr.getUserId() + ", MY ID~~~~~~~~~~~~~~~~~" + myId);
            if (!(cr.getUserId().equals(myId))) {
                crs2.add(cr);
            }
        }
        System.out.println("Print the number of conversation replies sent by ME i.e. number of messages outbound. ~~~~~~~~~~~~~~~~~~~~~~" + crs2.size());
        return crs2;
    }

    @Override
    public List<ConversationReply> findAllConversationRepliesInConversations(List<Conversation> conversations) {
        List<ConversationReply> crs = new ArrayList<>();
        for (Conversation c : conversations) {
            List<ConversationReply> cr = conversationReplyMongoTemplate.find(Query.query(Criteria.where("conversationId").is(c.getId())), ConversationReply.class, "ConversationReply");
            crs.addAll(cr);
        }        
        return crs;
    }

}
