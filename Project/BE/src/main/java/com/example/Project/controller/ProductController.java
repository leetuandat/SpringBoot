/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/6/2025
 * @time: 05:21 PM
 * @package: com.example.Project.controller
 */

package com.example.Project.controller;

import com.example.Project.dto.ProductDTO;
import com.example.Project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/multi")
    public ResponseEntity<List<ProductDTO>> findByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(productService.findByIds(ids));
    }


    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProduct(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(productService.findByProductName(keyword, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.save(productDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.update(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
