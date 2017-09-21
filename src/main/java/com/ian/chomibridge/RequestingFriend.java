package com.ian.chomibridge;

public class RequestingFriend {

    private int UserId;
    private int FriendId;
    private String FirstName;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public int getFriendId() {
        return FriendId;
    }

    public void setFriendId(int FriendId) {
        this.FriendId = FriendId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

}
