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
import java.util.List;

public interface PropertiesFileService {

    void createMyChomisSearchFile3(String msisdn, List<User> users, String language);

    void createMyProfileFile2(String msisdn, User user, String language);

    void createPostsByMeFile(String msisdn, List<Post> posts, String language);

    void createCommentsForPostFile2(String postMessage, String msisdn, List<com.ian.chomibridge.Comment> comments, boolean isNormalPost, String language, boolean showPrevious);

    void createMessagesMenuFile(String msisdn, int friendRequestsCount, int messageCount, int joinRequestsCount, int groupMessageCount, String language);

    void createFriendRequestsListMenuFile2(String msisdn, List<RequestingFriend> requestingFriends, String language);

    void createRequestingPersonProfile(String msisdn, RequestingFriend requestingFriend, String language);

    void createFriendRequestAccept(String msisdn, RequestingFriend requestingFriend, String language);

    void createMyChomisList2(String msisdn, List<User> users, String language);

    void createPostsByChomisFile(String msisdn, List<com.ian.chomibridge.Post> posts, boolean showPrevious, String language);

    void createMyChomisProfile(String msisdn, User user, String language);

    void getChomiRemove(String msisdn, User user, String language);

    void createChomiRemoveYes(String msisdn, User user, String language);

    void createDetailedMessageFile2(String msisdn, Mail mail, int read, String language);

    void createMessageReplyFile2(String msisdn, Mail mail, String language);

    void createTrendingsFile2(String msisdn, List<TopicCount> trendings, boolean showPrevious, String language);

    void createPostsListInTrendingTopicFile(String msisdn, List<com.ian.chomibridge.Post> posts, boolean showPrevious, String language);

    void createConversationsList(String msisdn, List<com.ian.chomibridge.Conversation> conversations, boolean showPrevious, String language);

    void createConversationRepliesListFile(String msisdn, List<Mail> mails, boolean showPrevious, String language);

    String readProperties(String code, String language);

    String readProperties(String code, String msisdn, String language);

    public void createSearchedProfile(String msisdn, User selectedUser, String language);

    public void createCommentOne(String msisdn, com.ian.chomibridge.Comment comment, String language);

    //Groups Addition
    void createPagesMenu(String msisdn, List<Group> groups, boolean showPrevious, String language);

    void createSeachPagesMenu(String msisdn, List<Group> groups, boolean showPrevious, String language);

    void createViewPageMenu(String msisdn, Group selectedGroup, boolean isFollowing, String language, int numberOfMembers);

    public void createViewSearchedPageMenu(String msisdn, Group selectedGroup, boolean isFollowing, String language, int numberOfMembers);

    public void createGroupMembers(String msisdn, List<User> users, boolean showPrevious, String language);

    void createGroupPostsFile(String msisdn, List<com.ian.chomibridge.Post> posts, boolean showPrevious, String language);

    void createMyPagesMenu(String msisdn, List<Group> groups, boolean showPrevious, String language);

    void createMyViewPageMenu(String msisdn, Group selectedGroup, boolean isFollowing, boolean isOwner, int pageRequestsCount, String language);

    void createMyViewPublicPageMenu(String msisdn, Group selectedGroup, boolean isFollowing, boolean isOwner, String language);

    public void createDeleteMyPublicPageMenu(String msisdn, Group selectedGroup, boolean isOwner, String language);

    public void createViewGroupMember(String msisdn, User selectedUser, boolean isOwner, String language);

    public void createPageJoinRequests(String msisdn, List<GroupRequestCounter> groupRequestCountersList, String language);

    void createPageRequestUsers(String msisdn, List<User> users, String pageName, String language);

    void createViewGroupRequestUserAcceptOrDecline(String msisdn, User user, String groupName, String language);
}
