package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.Document.DocumentRequest;
import org.jetblue.jetblue.Mapper.Document.DocumentResponse;
import org.jetblue.jetblue.Models.DAO.Document;
import org.jetblue.jetblue.Models.ENUM.DocumentType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {

    // Define methods related to document management here
    // For example, methods to upload, download, or delete documents
    // public void uploadDocument(Document document);
    // public Document downloadDocument(Long documentId);
    // public void deleteDocument(Long documentId);

    DocumentResponse setDocument(String passengerId, DocumentRequest document) throws Exception;
    /// This methode is used to upload a document for a passenger to S2 or S3 amazon remote storage
    ///
    /// @param passengerId The ID of the passenger to whom the document belongs.
    /// @param documentType The type of the document being uploaded.
    /// @param file The file to be uploaded, represented as a MultipartFile.
    DocumentResponse uploadDocument(Long passengerId, DocumentType documentType, MultipartFile file) throws IOException;
    List<DocumentResponse> getDocument(String passengerId) throws Exception;
    boolean deleteDocument(String passengerId, DocumentType documentType) throws Exception;

}
