/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.chomibridge.Group;
import com.ian.chomibridge.GroupRequestCounter;
import com.ian.chomibridge.Mail;
import com.ian.chomibridge.RequestingFriend;
import com.ian.chomibridge.TopicCount;
import com.ian.chomibridge.User;
import com.ian.entity.Post;
import com.ian.model.UssdMenu;
import java.util.List;

public interface UssdMenuService2 {

    List<UssdMenu> getTestMenu2(List<User> list, String msisdn, String language);

    List<UssdMenu> getMyProfile(User user, String msisdn, String language);

    List<UssdMenu> getMyPosts(List<Post> posts, String msisdn, String language);

    List<UssdMenu> getCommentsForPost2(List<com.ian.chomibridge.Comment> comments, String msisdn, String language);

    List<UssdMenu> getMessagesMenu(String msisdn, String language);

    List<UssdMenu> getFriendRequestList2(List<RequestingFriend> requestingFriends, String msisdn, String language);

    List<UssdMenu> getRequestingPersonProfile(String msisdn, String language);

    List<UssdMenu> getFriendRequestAccept(String msisdn, String language);

    List<UssdMenu> getMyChomis2(String msisdn, List<User> users, String language);

    List<UssdMenu> getPostsByChomisFile2(String msisdn, List<com.ian.chomibridge.Post> posts, String language);

    List<UssdMenu> getMyChomisProfile(User user, String msisdn, String language);

    List<UssdMenu> getChomiRemove(String msisdn, String language);

    List<UssdMenu> getChomiRemoveYes(String msisdn, String language);

    List<UssdMenu> getMessagesList4(String msisdn, List<Mail> mails, String language);

    List<UssdMenu> getMessagesList3(String msisdn, List<com.ian.chomibridge.Conversation> conversations, String language);

    List<UssdMenu> getDetailedMessages(String msisdn, String language);

    List<UssdMenu> getMessageReply(String msisdn, String language);

    List<UssdMenu> getTrendingList2(String msisdn, List<TopicCount> trendings, String language);

    List<UssdMenu> getTrendingOne(String msisdn, List<com.ian.chomibridge.Post> posts, String language);

    public List<UssdMenu> getSearchedProfile(User selectedUser, String msisdn, String language);

    public List<UssdMenu> getCommentOne(String msisdn, String language);

    //Groups Addition
    public List<UssdMenu> getPagesMenu(String msisdn, List<Group> groups, String language);

    public List<UssdMenu> getViewPageMenu(String msisdn, String language);

    public List<UssdMenu> createGroupMembersMenu(String msisdn, List<User> users, String language);

    public List<UssdMenu> getDeletePage(String msisdn, String language);

    public List<UssdMenu> createViewGroupMember(String msisdn, User selectedUser, String language);

    public List<UssdMenu> getPageJoinRequests(String msisdn, List<GroupRequestCounter> groupRequestCountersList, String language);

    public List<UssdMenu> getViewGroupRequestUserAcceptOrDecline(String msisdn, User selectedUser, String language);

    UssdMenu buildMenuItem(String code,
            String command,
            String order,
            String url,
            String page,
            String pageHeader,
            boolean display,
            boolean isShowOption,
            String value);

}
