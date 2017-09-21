package com.ian.chomibridge;

import java.util.Date;

public class Post {

    private int UserId;
    private int MessageId;
    private String Message;
    private Date DateCreated;
    private int LikesCount;
    private int Processed;
    private int Type;
    private boolean IsDeleted;
    private boolean ThisIsAShare;
    private int OrginalPostId;
    private boolean isGroupPost;
    private int GroupID;
    private String ShareMessage;
    private String firstName;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public int getMessageId() {
        return MessageId;
    }

    public void setMessageId(int MessageId) {
        this.MessageId = MessageId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public Date getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(Date DateCreated) {
        this.DateCreated = DateCreated;
    }

    public int getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(int LikesCount) {
        this.LikesCount = LikesCount;
    }

    public int getProcessed() {
        return Processed;
    }

    public void setProcessed(int Processed) {
        this.Processed = Processed;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public boolean getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(boolean IsDeleted) {
        this.IsDeleted = IsDeleted;
    }

    public boolean getThisIsAShare() {
        return ThisIsAShare;
    }

    public void setThisIsAShare(boolean ThisIsAShare) {
        this.ThisIsAShare = ThisIsAShare;
    }

    public int getOrginalPostId() {
        return OrginalPostId;
    }

    public void setOrginalPostId(int OrginalPostId) {
        this.OrginalPostId = OrginalPostId;
    }

    public boolean getIsGroupPost() {
        return isGroupPost;
    }

    public void setIsGroupPost(boolean isGroupPost) {
        this.isGroupPost = isGroupPost;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int GroupID) {
        this.GroupID = GroupID;
    }

    public String getShareMessage() {
        return ShareMessage;
    }

    public void setShareMessage(String ShareMessage) {
        this.ShareMessage = ShareMessage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
