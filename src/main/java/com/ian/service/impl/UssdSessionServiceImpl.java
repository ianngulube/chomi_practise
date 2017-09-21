/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.entity.UssdSession;
import com.ian.service.UssdSessionService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class UssdSessionServiceImpl implements UssdSessionService {

    @Autowired
    private MongoTemplate ussdSessionMongoTemplate;

    @Override
    public void saveUssdSession(UssdSession ussdSession) {
        if (!ussdSessionMongoTemplate.collectionExists(UssdSession.class)) {
            ussdSessionMongoTemplate.createCollection(UssdSession.class);
        }
        ussdSessionMongoTemplate.insert(ussdSession, "UssdSession");
    }

    @Override
    public List<UssdSession> listUssdSessions() {
        return ussdSessionMongoTemplate.findAll(UssdSession.class, "UssdSession");
    }

    @Override
    public UssdSession findUssdSession(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateUssdSession(UssdSession ussdSession) {
        UssdSession existingUssdSession = ussdSessionMongoTemplate.findOne(Query.query(Criteria.where("id").is(ussdSession.getId())), UssdSession.class, "UssdSession");
        existingUssdSession.setDateCreated(ussdSession.getDateCreated());
        existingUssdSession.setMsisdn(ussdSession.getMsisdn());
        existingUssdSession.setNextPage(ussdSession.getNextPage());
        existingUssdSession.setProvider(ussdSession.getProvider());
        existingUssdSession.setPage(ussdSession.getPage());
        existingUssdSession.setPersonId(ussdSession.getPersonId());
        existingUssdSession.setGenderInProvinceSearch(ussdSession.getGenderInProvinceSearch());
        existingUssdSession.setRequestingPersonId(ussdSession.getRequestingPersonId());
        existingUssdSession.setSelectedChomi(ussdSession.getSelectedChomi());
        existingUssdSession.setSelectedMessage(ussdSession.getSelectedMessage());
        existingUssdSession.setNextPostId(ussdSession.getNextPostId());
        existingUssdSession.setNextTrendingId(ussdSession.getNextTrendingId());
        existingUssdSession.setTrendingPosts(ussdSession.getTrendingPosts());
        existingUssdSession.setNextMessageId(ussdSession.getNextMessageId());
        existingUssdSession.setTopicSearchTitle(ussdSession.getTopicSearchTitle());
        existingUssdSession.setNextConversationId(ussdSession.getNextConversationId());
        existingUssdSession.setSelectedConversationId(ussdSession.getSelectedConversationId());
        existingUssdSession.setSelectedConversationReplyId(ussdSession.getSelectedConversationReplyId());
        existingUssdSession.setNextChomiId(ussdSession.getNextChomiId());
        existingUssdSession.setNavigationLevel(ussdSession.getNavigationLevel());
        existingUssdSession.setLastUpdateTime(ussdSession.getLastUpdateTime());
        existingUssdSession.setUser(ussdSession.getUser());
        existingUssdSession.setRequestingFriend(ussdSession.getRequestingFriend());
        existingUssdSession.setSearchedUsers(ussdSession.getSearchedUsers());
        existingUssdSession.setTopicCount(ussdSession.getTopicCount());
        existingUssdSession.setTrendingOneNextId(ussdSession.getTrendingOneNextId());
        existingUssdSession.setMail(ussdSession.getMail());
        existingUssdSession.setSelectedUser(ussdSession.getSelectedUser());
        existingUssdSession.setComments(ussdSession.getComments());
        existingUssdSession.setSelectedPost(ussdSession.getSelectedPost());
        existingUssdSession.setNextCommentId(ussdSession.getNextCommentId());
        existingUssdSession.setRead(ussdSession.getRead());
        existingUssdSession.setSelectedRequest(ussdSession.getSelectedRequest());
        existingUssdSession.setSelectedRequest2(ussdSession.getSelectedRequest2());
        existingUssdSession.setSelectedRequest3(ussdSession.getSelectedRequest3());
        existingUssdSession.setSelectedRequest4(ussdSession.getSelectedRequest4());
        existingUssdSession.setAdvertServed(ussdSession.isAdvertServed());
        existingUssdSession.setAdvertServed2(ussdSession.isAdvertServed2());
        existingUssdSession.setAdvertServed3(ussdSession.isAdvertServed3());
        existingUssdSession.setAdvertServed4(ussdSession.isAdvertServed4());
        existingUssdSession.setAdvertServed5(ussdSession.isAdvertServed5());
        existingUssdSession.setSubResponse(ussdSession.getSubResponse());
        existingUssdSession.setSubscribed(ussdSession.isSubscribed());
        existingUssdSession.setNotifiedOfNewChomi(ussdSession.isNotifiedOfNewChomi());
        existingUssdSession.setAcmScriptCalled(ussdSession.isAcmScriptCalled());
        //Groups Addition
        existingUssdSession.setNextPagesId(ussdSession.getNextPagesId());
        existingUssdSession.setGroupsSublist(ussdSession.getGroupsSublist());
        existingUssdSession.setSelectedGroup(ussdSession.getSelectedGroup());
        existingUssdSession.setNextGroupMemberId(ussdSession.getNextGroupMemberId());
        existingUssdSession.setUsersSublist(ussdSession.getUsersSublist());
        existingUssdSession.setGroupName(ussdSession.getGroupName());
        existingUssdSession.setGroupDescription(ussdSession.getGroupDescription());
        existingUssdSession.setPageType(ussdSession.getPageType());
        existingUssdSession.setNextGroupRequestCounterId(ussdSession.getNextGroupRequestCounterId());
        existingUssdSession.setGroupRequestCounters(ussdSession.getGroupRequestCounters());
        existingUssdSession.setGroupOwner(ussdSession.isGroupOwner());
        existingUssdSession.setCanPost(ussdSession.getCanPost());
        existingUssdSession.setSelectedGroupId(ussdSession.getSelectedGroupId());
        ussdSessionMongoTemplate.save(ussdSession, "UssdSession");
    }

    @Override
    public void deleteUssdSession(String id) {
        ussdSessionMongoTemplate.remove(Query.query(Criteria.where("id").is(id)), UssdSession.class, "UssdSession");
    }

    @Override
    public UssdSession findUssdSessionByMsisdn(String msisdn) {
        UssdSession ussdSession = ussdSessionMongoTemplate.findOne(Query.query(Criteria.where("msisdn").is(msisdn)), UssdSession.class, "UssdSession");
        return ussdSession;
    }

    @Override
    public void deleteOldUssdSessions() {
        List<UssdSession> sessions = ussdSessionMongoTemplate.findAll(UssdSession.class, "UssdSession");
        //System.out.println("Live Sessions-----------" + sessions.size() + "-------------");
        List<String> sessionIds = new ArrayList<>();
        for (UssdSession session : sessions) {
            if ((new Date().getTime() - session.getLastUpdateTime().getTime()) > 60000) {
                sessionIds.add(session.getId());
            }
        }
        //System.out.println("Expired Sessions-----------" + sessionIds.size() + "-------------");
        sessions = null;
        for (String id : sessionIds) {
            //System.out.println("Deleting expired session with id " + id);
            this.deleteUssdSession(id);
        }
    }

    @Override
    public void deleteUssdSessionByMsisdn(String msisdn) {
        try {
            ussdSessionMongoTemplate.remove(Query.query(Criteria.where("msisdn").is(msisdn)), UssdSession.class, "UssdSession");
            //System.out.println("---------------------------The session has been deleted--------------------------------------");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
