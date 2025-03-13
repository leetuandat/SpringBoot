package com.example.LabGuild05.service;

import com.example.LabGuild05.model.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(Long id);
    void savePost(Post post);
    void updatePost(Post post);
    void deletePost(Long id);
}
