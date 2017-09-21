/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.entity.Message;
import java.util.List;

public interface MessageService {

    void addMessage(Message message);

    void updateMessage(Message newMessage);

    List<Message> findAllMessages();

    List<Message> findAllMessagesByMsisdn(String msisdn);

    List<Message> findAllMessagesByTo(String to);

    void deleteMessage(String id);

    Message findMessageById(String id);
}
