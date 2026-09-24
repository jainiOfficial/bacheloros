package com.bacheloros.bacheloros_backend.dto.document;



import com.bacheloros.bacheloros_backend.entity.document.DocumentCategory;
import tools.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.util.Map;

public class CreateDocumentRequest {

    private DocumentCategory category;
    private String title;
    private String folderName;      // optional — agar diya, folder create/reuse hoga
    private LocalDate documentDate;
    private LocalDate expiryDate;
    private boolean reminderEnabled;
    private Map<String, Object> details;     // category-specific fields (policyNumber, brand, etc.)

    // getters and setters


    public DocumentCategory getCategory() {
        return category;
    }

    public void setCategory(DocumentCategory category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    public  Map<String, Object>  getDetails() {
        return details;
    }

    public void setDetails( Map<String, Object>  details) {
        this.details = details;
    }
}
