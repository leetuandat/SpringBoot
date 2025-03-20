/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson08
 * @date: 3/18/2025
 * @time: 07:11 PM
 * @package: com.example.Lesson08.service
 */

package com.example.Lesson08.service;

import com.example.Lesson08.entity.Author;
import com.example.Lesson08.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    //Lấy toàn bộ tác giả
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    //Lấy 1 tác giả
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    //Ghi lại
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    //Xóa
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    //Lấy ra những tác giả được chọn
    public List<Author> findAllById(List<Long> ids) {
        return authorRepository.findAllById(ids);
    }

}
