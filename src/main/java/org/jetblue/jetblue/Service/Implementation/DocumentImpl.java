package org.jetblue.jetblue.Service.Implementation;

import jakarta.transaction.Transactional;
import org.jetblue.jetblue.Mapper.Document.DocumentMapper;
import org.jetblue.jetblue.Mapper.Document.DocumentRequest;
import org.jetblue.jetblue.Mapper.Document.DocumentResponse;
import org.jetblue.jetblue.Models.DAO.Document;
import org.jetblue.jetblue.Models.DAO.Passenger;
import org.jetblue.jetblue.Models.ENUM.DocumentType;
import org.jetblue.jetblue.Repositories.DocumentRepo;
import org.jetblue.jetblue.Repositories.PassengerRepo;
import org.jetblue.jetblue.Service.DocumentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.jetblue.jetblue.Utils.DocumentUtils.createPassengerFolder;
import static org.jetblue.jetblue.Utils.DocumentUtils.deletePassengerFile;
import static org.jetblue.jetblue.Utils.PathEncoded.DecodeFilePath;
import static org.jetblue.jetblue.Utils.PathEncoded.EncodeFilePath;

@Service
public class DocumentImpl implements DocumentService {
    private final DocumentRepo documentRepo;
    private final PassengerRepo passengerRepo;
    private final S3Client s3Client;

    public DocumentImpl(DocumentRepo documentRepo, PassengerRepo passengerRepo, S3Client s3Client) {
        this.documentRepo = documentRepo;
        this.passengerRepo = passengerRepo;
        this.s3Client = s3Client;
    }

    // Stand By this method for future use
    @Override
    @Transactional
    public DocumentResponse uploadDocument(Long passengerId, DocumentType documentType, MultipartFile file) throws IOException {

        // 1. Find the passenger
        Passenger passenger = passengerRepo.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found with ID: " + passengerId));

        // 2. Generate a unique filename (the S3 key)
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // 3. Prepare metadata and upload the file to S3/B2
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("jetblue-documents-storage")
                    .key(originalFilename)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        } catch (Exception e) {
            // It's better to catch specific exceptions like AmazonS3Exception
            throw new IOException("Failed to upload file to S3: " + e.getMessage(), e);
        }

        // 4. Create the Document entity in the database
        Document newDocument = Document.builder()
                .type(documentType)
                .path(uniqueFilename) // Store the S3 key, not a local path
                .passenger(passenger)
                .build();

        documentRepo.save(newDocument);

        return DocumentMapper.toDocResponse(newDocument);
    }

    @Override
    public DocumentResponse setDocument(String passengerId, DocumentRequest document) throws Exception {
        if (passengerId == null || document == null) {
            throw new IllegalArgumentException("Passenger ID and Document cannot be null");
        }
        Passenger passenger = passengerRepo.findById(Long.parseLong(passengerId))
                .orElseThrow(() -> new Exception("Passenger not found with ID: " + passengerId));

        String passengerFolder = createPassengerFolder(
                passenger.getFirstName() + "_" + passenger.getLastName(),
                document.documentType().getDisplayName(),
                document.documentFile()
        );

        Document newDocument = Document.builder()
                .type(document.documentType())
                .path(EncodeFilePath(passengerFolder))
                .passenger(passenger)
                .build();

        documentRepo.save(newDocument);

        return DocumentMapper.toDocResponse(newDocument);
    }

    @Override
    public List<DocumentResponse> getDocument(String passengerId) throws Exception {
        return documentRepo.findByPassenger_Id(Long.parseLong(passengerId))
                .stream()
                .map(DocumentMapper::toDocResponse)
                .toList();
    }

    @Override
    public boolean deleteDocument(String passengerId, DocumentType documentType) throws Exception {
        if (passengerId == null || documentType == null) {
            throw new IllegalArgumentException("Passenger ID and Document Type cannot be null");
        }

        Passenger passenger = passengerRepo.findById(Long.parseLong(passengerId))
                .orElseThrow(() -> new Exception("Passenger not found with ID: " + passengerId));

        List<Document> documents = documentRepo.findByPassenger_IdAndType(passenger.getId(), documentType);
        if (!documents.isEmpty()) {
            for (Document document : documents) {
                deletePassengerFile(
                        DecodeFilePath(document.getPath())
                );
            }
        }
        documentRepo.deleteAll(documents);
        return true;
    }
}
