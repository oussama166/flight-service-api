package org.jetblue.jetblue.Controller;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Document.DocumentRequest;
import org.jetblue.jetblue.Models.ENUM.DocumentType;
import org.jetblue.jetblue.Service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.jetblue.jetblue.Utils.DocumentUtils.createPassengerFolder;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
public class DocumentController {
    private DocumentService documentService;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam("passengerId") String passengerId,
            @RequestParam("documentType") DocumentType documentType
    ) {
        try {
            File uploadDir = new File("uploads/");
            if (!uploadDir.exists()) uploadDir.mkdirs();

            // Save file path in DocumentRequest
            DocumentRequest documentRequest = DocumentRequest
                    .builder()
                    .documentUrl("")
                    .passengerId(passengerId)
                    .documentType(DocumentType.valueOf(documentType.name()))
                    .documentFile(file)
                    .build();

            documentService.setDocument(passengerId, documentRequest);
            return ResponseEntity.ok("Uploaded file: " + file.getOriginalFilename());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload file: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getFile")
    public ResponseEntity<?> getFile(
            @RequestParam("passengerId") String passengerId
    ) {
        try {
            var documents = documentService.getDocument(passengerId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not retrieve files: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(
            @RequestParam("passengerId") String passengerId,
            @RequestParam("documentType") DocumentType documentType
    ) {
        try {
            boolean deleted = documentService.deleteDocument(passengerId, documentType);
            if (deleted) {
                return ResponseEntity.ok("File deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("File not found or could not be deleted");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete file: " + e.getMessage());
        }
    }
}
