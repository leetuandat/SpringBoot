/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson08
 * @date: 3/18/2025
 * @time: 07:06 PM
 * @package: com.example.Lesson08.service
 */

package com.example.Lesson08.service;

import com.example.Lesson08.entity.Book;
import com.example.Lesson08.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    //Lấy danh sách Book
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    //Lấy ra 1 Book theo id
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    //Ghi lại những thay đổi
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    //Xóa Book theo id
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}
