package com.psh.blog_app.services;

import com.psh.blog_app.domain.CreatePostRequest;
import com.psh.blog_app.domain.UpdatePostRequest;
import com.psh.blog_app.domain.entities.Post;
import com.psh.blog_app.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post getPostById(UUID id);
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getAllDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
    void deletePostById(UUID id);
}
