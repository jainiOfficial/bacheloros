package com.bacheloros.bacheloros_backend.repository.document;

import com.bacheloros.bacheloros_backend.entity.document.Document;
import com.bacheloros.bacheloros_backend.entity.document.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findByUserIdAndCategory(Long userId, DocumentCategory category);

    List<Document> findByUserIdAndFolderId(Long userId, Long folderId);

    List<Document> findByUserId(Long userId);

    List<Document> findByUserIdAndTitleContainingIgnoreCase(Long userId, String search);

    List<Document> findByUserIdAndExpiryDateBetween(Long userId, LocalDate start, LocalDate end);
}
