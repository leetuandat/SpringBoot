/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 4/24/2025
 * @time: 04:56 PM
 * @package: com.example.Project.service
 */

package com.example.Project.service;

import com.example.Project.dto.AuthorDTO;
import java.util.List;

public interface AuthorService {
    AuthorDTO createAuthor(AuthorDTO dto);
    AuthorDTO getAuthorById(Long id);
    List<AuthorDTO> getAllAuthors();
    AuthorDTO updateAuthor(Long id, AuthorDTO dto);
    void deleteAuthor(Long id);
    List<AuthorDTO> findByAuthorName(String authorName);
}
