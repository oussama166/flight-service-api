package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.Document.DocumentRequest;
import org.jetblue.jetblue.Mapper.Document.DocumentResponse;
import org.jetblue.jetblue.Models.DAO.Document;
import org.jetblue.jetblue.Models.ENUM.DocumentType;

import java.util.List;

public interface DocumentService {

    // Define methods related to document management here
    // For example, methods to upload, download, or delete documents
    // public void uploadDocument(Document document);
    // public Document downloadDocument(Long documentId);
    // public void deleteDocument(Long documentId);

    DocumentResponse setDocument(String passengerId, DocumentRequest document) throws Exception;
    List<DocumentResponse> getDocument(String passengerId) throws Exception;
    boolean deleteDocument(String passengerId, DocumentType documentType) throws Exception;

}
