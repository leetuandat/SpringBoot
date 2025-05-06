/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/6/2025
 * @time: 05:37 PM
 * @package: com.example.Project.controller
 */

package com.example.Project.controller;

import com.example.Project.dto.NewsDTO;
import com.example.Project.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<Page<NewsDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(newsService.findAll(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NewsDTO>> searchNews(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(newsService.findByName(keyword, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.findById(id));
    }

    @PostMapping
    public ResponseEntity<NewsDTO> createNews(@RequestBody NewsDTO newsDTO) {
        return ResponseEntity.ok(newsService.save(newsDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsDTO> updateNews(@PathVariable Long id, @RequestBody NewsDTO newsDTO) {
        return ResponseEntity.ok(newsService.update(id, newsDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
