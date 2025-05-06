/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/6/2025
 * @time: 05:52 PM
 * @package: com.example.Project.controller
 */

package com.example.Project.controller;

import com.example.Project.dto.AuthorDTO;
import com.example.Project.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> findAll() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/search")
    public List<AuthorDTO> search(@RequestParam String name) {
        return authorService.findByAuthorName(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> create(@RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.createAuthor(authorDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> update(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.updateAuthor(id, authorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
