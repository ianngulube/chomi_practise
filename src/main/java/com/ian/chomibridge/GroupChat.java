package com.ian.chomibridge;

import java.util.Date;

public class GroupChat {

    private int groupChatId;
    private int groupId;
    private int userId;
    private String subject;
    private String message;
    private Date date;
    private int read;
    private int typeId;
    private int originalMailId;
    private int contentId;
    private int toUserDel;
    private int fromUserDel;

    public int getGroupChatId() {
        return groupChatId;
    }

    public void setGroupChatId(int groupChatId) {
        this.groupChatId = groupChatId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getOriginalMailId() {
        return originalMailId;
    }

    public void setOriginalMailId(int originalMailId) {
        this.originalMailId = originalMailId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getToUserDel() {
        return toUserDel;
    }

    public void setToUserDel(int toUserDel) {
        this.toUserDel = toUserDel;
    }

    public int getFromUserDel() {
        return fromUserDel;
    }

    public void setFromUserDel(int fromUserDel) {
        this.fromUserDel = fromUserDel;
    }

}
