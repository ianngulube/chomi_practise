/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.entity.Comment;
import java.util.List;

public interface CommentService {

    void addComment(Comment comment);

    void updatePost(Comment newComment);

    List<Comment> findCommentsByPostId(String postId);

    void deleteComment(String id);
}
