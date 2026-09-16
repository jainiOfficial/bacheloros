package com.bacheloros.bacheloros_backend.controller;

import com.bacheloros.bacheloros_backend.dto.document.CreateDocumentRequest;
import com.bacheloros.bacheloros_backend.dto.document.DocumentResponse;
import com.bacheloros.bacheloros_backend.service.document.DocumentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public DocumentResponse createDocument(
            @RequestPart("data") CreateDocumentRequest request,
            @RequestPart("file") MultipartFile file) {
        return documentService.createDocument(request, file);
    }

    @GetMapping("/{id}")
    public DocumentResponse getDocument(@PathVariable Long id) {
        return documentService.getDocumentById(id);
    }
}
