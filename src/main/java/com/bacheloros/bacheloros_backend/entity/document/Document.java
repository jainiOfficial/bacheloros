package com.bacheloros.bacheloros_backend.entity.document;


import com.bacheloros.bacheloros_backend.entity.User;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;   // nullable — no folder means directly under category

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String fileKey;   // S3 object key

    @Column(nullable = false)
    private String fileName;  // original filename, for display

    private Long fileSize;

    private String mimeType;

    private LocalDate documentDate;
    private LocalDate expiryDate;

    private boolean reminderEnabled = false;


    //@JdbcTypeCode(SqlTypes.JSON) + columnDefinition = "jsonb" — yehi jodi hai jo Postgres ko batati hai "ye column ek JSONB type ka hai, plain text nahi." Hibernate 6 mein ye built-in support hai (purani Hibernate versions mein ek extra library hypersistence-utils chahiye hoti thi — abhi zaroorat nahi).
    //JsonNode (Jackson ki class, already Spring Boot mein available hai) — ye ek flexible "kuch bhi JSON" type hai. Iske andar hum kisi bhi category ka koi bhi shape ka JSON daal sakte hain (Receipt ka storeName, Insurance ka policyNumber, waghera) — bina Java mein fixed fields banaye. Jab hum service layer likhenge, wahan hum is JSON ko category ke hisaab se validate karenge.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> details;
//    private JsonNode details;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //@PrePersist / @PreUpdate — ye JPA lifecycle hooks hain, automatically call hote hain jab entity save/update ho rahi ho, taaki createdAt/updatedAt manually set na karna pade har jagah.
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DocumentCategory getCategory() {
        return category;
    }

    public void setCategory(DocumentCategory category) {
        this.category = category;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
