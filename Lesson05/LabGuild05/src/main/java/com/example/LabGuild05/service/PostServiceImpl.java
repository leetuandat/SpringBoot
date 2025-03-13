/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild05
 * @date: 3/11/2025
 * @time: 05:24 PM
 * @package: com.example.LabGuild05.service
 */

package com.example.LabGuild05.service;

import com.example.LabGuild05.model.Post;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class PostServiceImpl implements PostService {
    private final List<Post> posts = new ArrayList<>();
    private Long nextId = 1L;

    public PostServiceImpl() {
        posts.add(new Post(nextId++, "Bài viết 1", "Nội dung bài viết 1"));
        posts.add(new Post(nextId++, "Bài viết 2", "Nội dung bài viết 2"));
        posts.add(new Post(nextId++, "Bài viết 3", "Nội dung bài viết 3"));
        posts.add(new Post(nextId++, "Bài viết 4", "Nội dung bài viết 4"));
        posts.add(new Post(nextId++, "Bài viết 5", "Nội dung bài viết 5"));
    }

    @Override
    public List<Post> getAllPosts() {
        return new ArrayList<>(posts);
    }

    @Override
    public Post getPostById(Long id) {
        return posts.stream().filter(post -> post.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void savePost(Post post) {
        if (post.getTitle() != null && post.getContent() != null) {
            post.setId(nextId++);
            posts.add(post);
            System.out.println("Post added successfully: " + post);
        } else {
            System.out.println("Invalid post data!");
        }
    }


    @Override
    public void updatePost(Post post) {
        Post existingPost = getPostById(post.getId());
        if (existingPost != null) {
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
        }
    }

    @Override
    public void deletePost(Long id) {
        posts.removeIf(post -> post.getId().equals(id));
    }
}
