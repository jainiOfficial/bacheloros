package com.bacheloros.bacheloros_backend.service.document;

import com.bacheloros.bacheloros_backend.dto.document.CreateDocumentRequest;
import com.bacheloros.bacheloros_backend.dto.document.DocumentResponse;
import com.bacheloros.bacheloros_backend.entity.User;
import com.bacheloros.bacheloros_backend.entity.document.Document;
import com.bacheloros.bacheloros_backend.entity.document.DocumentCategory;
import com.bacheloros.bacheloros_backend.entity.document.Folder;
import com.bacheloros.bacheloros_backend.exception.ResourceNotFoundException;
import com.bacheloros.bacheloros_backend.repository.UserRepository;
import com.bacheloros.bacheloros_backend.repository.document.DocumentRepository;
import com.bacheloros.bacheloros_backend.repository.document.FolderRepository;
import com.bacheloros.bacheloros_backend.shared.storage.S3Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public DocumentService(DocumentRepository documentRepository, FolderRepository folderRepository, UserRepository userRepository, S3Service s3Service) {
        this.documentRepository = documentRepository;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Folder resolveFolder(User user,CreateDocumentRequest request) {
        String folderName=request.getFolderName();
        if(folderName==null || folderName.isBlank())
            return null;
        return folderRepository.findByUserIdAndCategoryAndName(user.getId(),request.getCategory(),folderName).orElseGet(()->{
            Folder newFolder = new Folder();
            newFolder.setUser(user);
            newFolder.setCategory(request.getCategory());
            newFolder.setName(folderName);
            return folderRepository.save(newFolder);
        });
    }

    private DocumentResponse toResponse(Document document, boolean includeViewUrl) {
        DocumentResponse response = new DocumentResponse();
        response.setId(document.getId());
        response.setCategory(document.getCategory());
        response.setFolderName(document.getFolder() != null ? document.getFolder().getName() : null);
        response.setTitle(document.getTitle());
        response.setFileName(document.getFileName());
        response.setFileSize(document.getFileSize());
        response.setMimeType(document.getMimeType());
        response.setDocumentDate(document.getDocumentDate());
        response.setExpiryDate(document.getExpiryDate());
        response.setReminderEnabled(document.isReminderEnabled());
        response.setDetails(document.getDetails());
        response.setCreatedAt(document.getCreatedAt());

        if (includeViewUrl) {
            response.setViewUrl(s3Service.generatePresignedGetUrl(document.getFileKey()));
        }

        return response;
    }

    public DocumentResponse createDocument(CreateDocumentRequest request, MultipartFile file)
    {
        User user=getCurrentUser();
        String fileKey=s3Service.uploadFile( file,user.getId());
        Folder folder=resolveFolder(user,request);


        Document document = new Document();
        document.setUser(user);
        document.setCategory(request.getCategory());
        document.setFolder(folder);
        document.setTitle(request.getTitle());
        document.setFileKey(fileKey);
        document.setFileName(file.getOriginalFilename());
        document.setFileSize(file.getSize());
        document.setMimeType(file.getContentType());
        document.setDocumentDate(request.getDocumentDate());
        document.setExpiryDate(request.getExpiryDate());
        document.setReminderEnabled(request.isReminderEnabled());
        document.setDetails(request.getDetails());

        Document saved = documentRepository.save(document);
        return toResponse(saved, false);
    }

    public DocumentResponse getDocumentById(Long id) {
        User currentUser = getCurrentUser();

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        if (!document.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Document not found");
        }

        return toResponse(document, true);
    }
}
