/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.entity.Conversation;
import java.util.List;

public interface ConversationService {

    void addConversation(Conversation conversation);

    void updateConversation(Conversation newConversation);

    List<Conversation> findAllConversations();

    void deleteConversation(String id);

    Conversation findConversationById(String id);

    Conversation findConversationBetweenTwo(String userOne, String userTwo);

    List<Conversation> findMyConversations(String personId);

    List<Conversation> findConversations(List<String> ids);
}
