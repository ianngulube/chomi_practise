/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.entity.Post;
import com.ian.service.PostService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private MongoTemplate postMongoTemplate;

    @Override
    public void savePost(Post post) {
        if (!postMongoTemplate.collectionExists(Post.class)) {
            postMongoTemplate.createCollection(Post.class);
        }
        postMongoTemplate.insert(post, "Post");
    }

    @Override
    public void updatePost(Post newPost) {
        Post post = postMongoTemplate.findOne(Query.query(Criteria.where("id").is(newPost.getId())), Post.class, "Post");
        post.setPostedBy(newPost.getPostedBy());
        post.setText(newPost.getText());
        post.setTime(newPost.getTime());
        postMongoTemplate.save(post, "Post");
    }

    @Override
    public List<Post> findAllPosts() {
        return postMongoTemplate.findAll(Post.class, "Post");
    }

    @Override
    public Post findPostById(String id) {
        return postMongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Post.class, "Post");
    }

    @Override
    public Post findPostByDateRange(Date id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletePost() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Post findPostByPostedBy(String postedBy) {
        return postMongoTemplate.findOne(Query.query(Criteria.where("postedBy").is(postedBy)), Post.class, "Post");
    }

    @Override
    public List<Post> findAllPostsByPostedBy(String postedBy) {
        return postMongoTemplate.find(Query.query(Criteria.where("postedBy").is(postedBy)), Post.class, "Post");
    }

    @Override
    public List<Post> findAllPostsPostedByChomis(List<String> friendIds) {
        List<Post> posts = null;
        if (friendIds != null) {
            posts = new ArrayList<>();
            for (String id : friendIds) {
                List<Post> pList = this.postMongoTemplate.find(Query.query(Criteria.where("postedBy").is(id)), Post.class, "Post");
                for (Post p : pList) {
                    if (p != null) {
                        posts.add(p);
                    }
                }
            }
        }
        if ((posts == null)) {
            System.out.println("%%%%%%%% we return null");
            return null;
        } else {
            return posts;
        }
    }

    @Override
    public List<Post> findPostsGivenAListOfPostIds(List<String> postIds) {
        List<Post> posts = null;
        if (postIds != null) {
            posts = new ArrayList<>();
            for (String id : postIds) {
                Post p = this.postMongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Post.class, "Post");
                posts.add(p);
            }
        }
        if ((posts == null)) {
            System.out.println("%%%%%%%% we return null");
            return null;
        } else {
            return posts;
        }
    }

}
