package com.ian.chomibridge;

import java.util.Date;

public class Mail {

    protected int mailId;
    protected int toUserId;
    protected int fromUserId;
    protected String subject;
    protected String message;
    protected Date date;
    protected int read;
    protected int typeId;
    protected int originalMailId;
    protected int contentId;
    protected int toUserDel;
    protected int fromUserDel;
    protected String firstName;

    public int getMailId() {
        return mailId;
    }

    public void setMailId(int mailId) {
        this.mailId = mailId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
