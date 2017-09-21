/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.entity.Conversation;
import com.ian.entity.ConversationReply;
import java.util.List;

public interface ConversationReplyService {

    void addConversationReply(ConversationReply conversationReply);

    void updateConversationReply(ConversationReply newConversationReply);

    List<ConversationReply> findAllConversationReplies();

    void deleteConversationReply(String id);

    ConversationReply findConversationReplyById(String id);

    List<ConversationReply> findAllConversationsByConversationId(String conversationId);

    List<ConversationReply> findAllConversationRepliesSentByMe(String myId, List<Conversation> myConversations);

    List<ConversationReply> findAllConversationRepliesInConversations(List<Conversation> conversations);
}
