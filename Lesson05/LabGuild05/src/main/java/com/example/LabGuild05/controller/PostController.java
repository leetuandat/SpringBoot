/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild05
 * @date: 3/11/2025
 * @time: 05:33 PM
 * @package: com.example.LabGuild05.controller
 */

package com.example.LabGuild05.controller;

import com.example.LabGuild05.model.Post;
import com.example.LabGuild05.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        model.addAttribute("title", "Danh Sách Bài Viết");
        model.addAttribute("content", "list::content");
        return "layout";
    }

    @GetMapping("/post/add")
    public String addPostForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("title", "Thêm Bài Viết");
        model.addAttribute("content", "add::content");
        return "layout";
    }

    @PostMapping("/post/add")
    public String savePost(@ModelAttribute Post post) {
        postService.savePost(post);
        return "redirect:/posts";
    }

    @GetMapping("/post/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        if(post == null) {
            return "redirect:/posts";
        }
        model.addAttribute("post", post);
        model.addAttribute("title", "Sửa Bài Viết");
        model.addAttribute("content", "edit::content");
        return "layout";
    }

    @PostMapping("/post/edit")
    public String updatePost(@ModelAttribute Post post) {
        postService.updatePost(post);
        return "redirect:/posts";
    }

    @GetMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }
}
