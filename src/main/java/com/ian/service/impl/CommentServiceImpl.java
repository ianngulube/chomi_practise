/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.entity.Comment;
import com.ian.service.CommentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    private MongoTemplate commentMongoTemplate;
    
    @Override
    public void addComment(Comment comment) {
        if (!commentMongoTemplate.collectionExists(Comment.class)) {
            commentMongoTemplate.createCollection(Comment.class);
        }
        commentMongoTemplate.insert(comment, "Comment");
    }
    
    @Override
    public void updatePost(Comment newComment) {
        Comment comment = commentMongoTemplate.findOne(Query.query(Criteria.where("id").is(newComment.getId())), Comment.class, "Comment");
        comment.setPostId(newComment.getPostId());
        comment.setPostedBy(newComment.getPostedBy());
        comment.setText(newComment.getText());
        comment.setTime(newComment.getTime());
        comment.setPostedByUsername(newComment.getPostedByUsername());
        commentMongoTemplate.save(comment, "Comment");
    }
    
    @Override
    public List<Comment> findCommentsByPostId(String postId) {
        return commentMongoTemplate.find(Query.query(Criteria.where("postId").is(postId)), Comment.class, "Comment");
    }
    
    @Override
    public void deleteComment(String id) {
        commentMongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Comment.class, "Comment");
    }
    
}
