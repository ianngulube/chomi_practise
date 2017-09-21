/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.entity.Post;
import java.util.Date;
import java.util.List;

public interface PostService {

    void savePost(Post post);

    void updatePost(Post newPost);

    List<Post> findAllPosts();

    Post findPostById(String id);

    Post findPostByDateRange(Date id);

    void deletePost();

    Post findPostByPostedBy(String postedBy);

    List<Post> findAllPostsByPostedBy(String postedBy);

    List<Post> findAllPostsPostedByChomis(List<String> friendIds);

    List<Post> findPostsGivenAListOfPostIds(List<String> postIds);
}
