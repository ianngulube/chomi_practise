package com.ian.model;

import java.util.Date;

public class GroupUser {

    private int groupId;
    private int userId;
    private int isApproved;
    private Date created;
    private int hasApplied;
    private int following;
    private int adminUser;

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

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getHasApplied() {
        return hasApplied;
    }

    public void setHasApplied(int hasApplied) {
        this.hasApplied = hasApplied;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(int adminUser) {
        this.adminUser = adminUser;
    }

}
