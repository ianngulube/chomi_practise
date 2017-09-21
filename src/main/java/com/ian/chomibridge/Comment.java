package com.ian.chomibridge;

import java.util.Date;

public class Comment {

    private int CommentId;
    private int MessageId;
    private int UserId;
    private String Comment;
    private Date CommentDate;
    private int Type;
    private int Processed;
    private String FirstName;

    public int getCommentId() {
        return CommentId;
    }

    public void setCommentId(int CommentId) {
        this.CommentId = CommentId;
    }

    public int getMessageId() {
        return MessageId;
    }

    public void setMessageId(int MessageId) {
        this.MessageId = MessageId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    public Date getCommentDate() {
        return CommentDate;
    }

    public void setCommentDate(Date CommentDate) {
        this.CommentDate = CommentDate;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getProcessed() {
        return Processed;
    }

    public void setProcessed(int Processed) {
        this.Processed = Processed;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

}
