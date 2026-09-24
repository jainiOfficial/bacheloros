package com.bacheloros.bacheloros_backend.dto.document;

import com.bacheloros.bacheloros_backend.entity.document.DocumentCategory;

public class FolderResponse {
    private Long id;
    private DocumentCategory category;
    private String name;

    //Getter And Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentCategory getCategory() {
        return category;
    }

    public void setCategory(DocumentCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
