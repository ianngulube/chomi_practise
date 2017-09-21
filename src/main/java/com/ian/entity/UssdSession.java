/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.entity;

import com.ian.chomibridge.Comment;
import com.ian.chomibridge.Group;
import com.ian.chomibridge.GroupRequestCounter;
import com.ian.chomibridge.Mail;
import com.ian.chomibridge.Post;
import com.ian.chomibridge.RequestingFriend;
import com.ian.chomibridge.TopicCount;
import com.ian.chomibridge.User;
import com.ian.model.SubResponse;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;

public class UssdSession {

    @Id
    private String id;
    private String msisdn;
    private String provider;
    private Date dateCreated;
    private String nextPage;
    private String page;
    private String postId;
    private String personId;
    private String searchName;
    private String genderInProvinceSearch;
    private String selectedChomi;
    private String selectedMessage;
    private int nextPostId;
    private int nextTrendingId;
    private List<com.ian.chomibridge.Post> trendingPosts;
    private int nextMessageId;
    private String topicSearchTitle;
    private String requestingPersonId;
    private String language;
    private String username;
    private int age;
    private String gender;
    private String province;
    private int nextConversationId;
    private String selectedConversationId;
    private String selectedConversationReplyId;
    private int nextChomiId;
    private int navigationLevel;
    private Date lastUpdateTime;
    private User user;
    private RequestingFriend requestingFriend;
    private List<com.ian.chomibridge.Post> myPosts;
    private List<User> searchedUsers;
    private List<TopicCount> topicCount;
    private int trendingOneNextId;
    private Mail mail;
    private User selectedUser;
    private List<com.ian.chomibridge.Comment> comments;
    private com.ian.chomibridge.Post selectedPost;
    private int nextCommentId;
    private int read;
    private String selectedRequest;
    private String selectedRequest2;
    private String selectedRequest3;
    private String selectedRequest4;
    private boolean advertServed;
    private boolean advertServed2;
    private boolean advertServed3;
    private boolean advertServed4;
    private boolean advertServed5;
    private SubResponse subResponse;
    private boolean subscribed;
    private boolean notifiedOfNewChomi;
    private boolean acmScriptCalled;
    //Groups Addition
    private int nextPagesId;
    private List<Group> groupsSublist;
    private Group selectedGroup;
    private int nextGroupMemberId;
    private List<User> usersSublist;
    private String groupName;
    private String groupDescription;
    private int pageType;
    private String groupSearchName;
    private int nextGroupRequestCounterId;
    private List<GroupRequestCounter> groupRequestCounters;
    private boolean groupOwner;
    private int canPost;
    private int selectedGroupId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getGenderInProvinceSearch() {
        return genderInProvinceSearch;
    }

    public void setGenderInProvinceSearch(String genderInProvinceSearch) {
        this.genderInProvinceSearch = genderInProvinceSearch;
    }

    public String getRequestingPersonId() {
        return requestingPersonId;
    }

    public void setRequestingPersonId(String requestingPersonId) {
        this.requestingPersonId = requestingPersonId;
    }

    public String getSelectedChomi() {
        return selectedChomi;
    }

    public void setSelectedChomi(String selectedChomi) {
        this.selectedChomi = selectedChomi;
    }

    public String getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(String selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public int getNextPostId() {
        return nextPostId;
    }

    public void setNextPostId(int nextPostId) {
        this.nextPostId = nextPostId;
    }

    public int getNextTrendingId() {
        return nextTrendingId;
    }

    public void setNextTrendingId(int nextTrendingId) {
        this.nextTrendingId = nextTrendingId;
    }

    public List<com.ian.chomibridge.Post> getTrendingPosts() {
        return trendingPosts;
    }

    public void setTrendingPosts(List<com.ian.chomibridge.Post> trendingPosts) {
        this.trendingPosts = trendingPosts;
    }

    public int getNextMessageId() {
        return nextMessageId;
    }

    public void setNextMessageId(int nextMessageId) {
        this.nextMessageId = nextMessageId;
    }

    public String getTopicSearchTitle() {
        return topicSearchTitle;
    }

    public void setTopicSearchTitle(String topicSearchTitle) {
        this.topicSearchTitle = topicSearchTitle;
    }

    public int getNextConversationId() {
        return nextConversationId;
    }

    public void setNextConversationId(int nextConversationId) {
        this.nextConversationId = nextConversationId;
    }

    public String getSelectedConversationId() {
        return selectedConversationId;
    }

    public void setSelectedConversationId(String selectedConversationId) {
        this.selectedConversationId = selectedConversationId;
    }

    public String getSelectedConversationReplyId() {
        return selectedConversationReplyId;
    }

    public void setSelectedConversationReplyId(String selectedConversationReplyId) {
        this.selectedConversationReplyId = selectedConversationReplyId;
    }

    public int getNextChomiId() {
        return nextChomiId;
    }

    public void setNextChomiId(int nextChomiId) {
        this.nextChomiId = nextChomiId;
    }

    public int getNavigationLevel() {
        return navigationLevel;
    }

    public void setNavigationLevel(int navigationLevel) {
        this.navigationLevel = navigationLevel;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RequestingFriend getRequestingFriend() {
        return requestingFriend;
    }

    public void setRequestingFriend(RequestingFriend requestingFriend) {
        this.requestingFriend = requestingFriend;
    }

    public List<com.ian.chomibridge.Post> getMyPosts() {
        return myPosts;
    }

    public void setMyPosts(List<com.ian.chomibridge.Post> myPosts) {
        this.myPosts = myPosts;
    }

    public List<User> getSearchedUsers() {
        return searchedUsers;
    }

    public void setSearchedUsers(List<User> searchedUsers) {
        this.searchedUsers = searchedUsers;
    }

    public List<TopicCount> getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(List<TopicCount> topicCount) {
        this.topicCount = topicCount;
    }

    public int getTrendingOneNextId() {
        return trendingOneNextId;
    }

    public void setTrendingOneNextId(int trendingOneNextId) {
        this.trendingOneNextId = trendingOneNextId;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Post getSelectedPost() {
        return selectedPost;
    }

    public void setSelectedPost(Post selectedPost) {
        this.selectedPost = selectedPost;
    }

    public int getNextCommentId() {
        return nextCommentId;
    }

    public void setNextCommentId(int nextCommentId) {
        this.nextCommentId = nextCommentId;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(String selectedRequest) {
        this.selectedRequest = selectedRequest;
    }

    public String getSelectedRequest2() {
        return selectedRequest2;
    }

    public void setSelectedRequest2(String selectedRequest2) {
        this.selectedRequest2 = selectedRequest2;
    }

    public String getSelectedRequest3() {
        return selectedRequest3;
    }

    public void setSelectedRequest3(String selectedRequest3) {
        this.selectedRequest3 = selectedRequest3;
    }

    public String getSelectedRequest4() {
        return selectedRequest4;
    }

    public void setSelectedRequest4(String selectedRequest4) {
        this.selectedRequest4 = selectedRequest4;
    }

    public boolean isAdvertServed() {
        return advertServed;
    }

    public void setAdvertServed(boolean advertServed) {
        this.advertServed = advertServed;
    }

    public boolean isAdvertServed2() {
        return advertServed2;
    }

    public void setAdvertServed2(boolean advertServed2) {
        this.advertServed2 = advertServed2;
    }

    public boolean isAdvertServed3() {
        return advertServed3;
    }

    public void setAdvertServed3(boolean advertServed3) {
        this.advertServed3 = advertServed3;
    }

    public boolean isAdvertServed4() {
        return advertServed4;
    }

    public void setAdvertServed4(boolean advertServed4) {
        this.advertServed4 = advertServed4;
    }

    public boolean isAdvertServed5() {
        return advertServed5;
    }

    public void setAdvertServed5(boolean advertServed5) {
        this.advertServed5 = advertServed5;
    }

    public SubResponse getSubResponse() {
        return subResponse;
    }

    public void setSubResponse(SubResponse subResponse) {
        this.subResponse = subResponse;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public boolean isNotifiedOfNewChomi() {
        return notifiedOfNewChomi;
    }

    public void setNotifiedOfNewChomi(boolean notifiedOfNewChomi) {
        this.notifiedOfNewChomi = notifiedOfNewChomi;
    }

    public boolean isAcmScriptCalled() {
        return acmScriptCalled;
    }

    public void setAcmScriptCalled(boolean acmScriptCalled) {
        this.acmScriptCalled = acmScriptCalled;
    }

    public int getNextPagesId() {
        return nextPagesId;
    }

    public void setNextPagesId(int nextPagesId) {
        this.nextPagesId = nextPagesId;
    }

    public List<Group> getGroupsSublist() {
        return groupsSublist;
    }

    public void setGroupsSublist(List<Group> groupsSublist) {
        this.groupsSublist = groupsSublist;
    }

    public Group getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public int getNextGroupMemberId() {
        return nextGroupMemberId;
    }

    public void setNextGroupMemberId(int nextGroupMemberId) {
        this.nextGroupMemberId = nextGroupMemberId;
    }

    public List<User> getUsersSublist() {
        return usersSublist;
    }

    public void setUsersSublist(List<User> usersSublist) {
        this.usersSublist = usersSublist;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public int getPageType() {
        return pageType;
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    public String getGroupSearchName() {
        return groupSearchName;
    }

    public void setGroupSearchName(String groupSearchName) {
        this.groupSearchName = groupSearchName;
    }

    public int getNextGroupRequestCounterId() {
        return nextGroupRequestCounterId;
    }

    public void setNextGroupRequestCounterId(int nextGroupRequestCounterId) {
        this.nextGroupRequestCounterId = nextGroupRequestCounterId;
    }

    public List<GroupRequestCounter> getGroupRequestCounters() {
        return groupRequestCounters;
    }

    public void setGroupRequestCounters(List<GroupRequestCounter> groupRequestCounters) {
        this.groupRequestCounters = groupRequestCounters;
    }

    public boolean isGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(boolean groupOwner) {
        this.groupOwner = groupOwner;
    }

    public int getCanPost() {
        return canPost;
    }

    public void setCanPost(int canPost) {
        this.canPost = canPost;
    }

    public int getSelectedGroupId() {
        return selectedGroupId;
    }

    public void setSelectedGroupId(int selectedGroupId) {
        this.selectedGroupId = selectedGroupId;
    }

}
