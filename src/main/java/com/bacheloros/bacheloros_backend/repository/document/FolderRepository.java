package com.bacheloros.bacheloros_backend.repository.document;

import com.bacheloros.bacheloros_backend.entity.document.DocumentCategory;
import com.bacheloros.bacheloros_backend.entity.document.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder,Long> {
    List<Folder> findByUserIdAndCategory(Long userId, DocumentCategory category);

    Optional<Folder> findByUserIdAndCategoryAndName(Long userId, DocumentCategory category, String name);
}
