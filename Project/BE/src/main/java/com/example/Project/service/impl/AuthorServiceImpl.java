/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 4/24/2025
 * @time: 04:56 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.AuthorDTO;
import com.example.Project.entity.Author;
import com.example.Project.mapper.AuthorMapper;
import com.example.Project.repository.AuthorRepository;
import com.example.Project.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;
    private AuthorMapper authorMapper;

    @Override
    public AuthorDTO createAuthor(AuthorDTO dto) {
        Author author = authorMapper.toEntity(dto);
        return authorMapper.toDTO(authorRepository.save(author));
    }

    @Override
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found with id: " + id));
        return authorMapper.toDTO(author);
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDTO updateAuthor(Long id, AuthorDTO dto) {
        Author existing = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found with id: " + id));
        existing.setName(dto.name());
        existing.setAuthorKey(dto.authorKey());
        existing.setBiography(dto.biography());
        existing.setImage(dto.image());
        return authorMapper.toDTO(authorRepository.save(existing));
    }

    @Override
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new NoSuchElementException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }
}
