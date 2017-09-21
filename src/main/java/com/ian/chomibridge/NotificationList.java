package com.ian.chomibridge;

import java.util.Date;

public class NotificationList {

    private int userID;
    private String notificationText;
    private int notificationType;
    private int iIsNotificationRead;
    private Date created;
    private Date modified;
    private int contentID;
    private int senderID;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public int getiIsNotificationRead() {
        return iIsNotificationRead;
    }

    public void setiIsNotificationRead(int iIsNotificationRead) {
        this.iIsNotificationRead = iIsNotificationRead;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public int getContentID() {
        return contentID;
    }

    public void setContentID(int contentID) {
        this.contentID = contentID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

}
