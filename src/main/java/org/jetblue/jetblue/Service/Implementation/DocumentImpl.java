package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Document.DocumentMapper;
import org.jetblue.jetblue.Mapper.Document.DocumentResponse;
import org.jetblue.jetblue.Mapper.Document.DocumentRequest;
import org.jetblue.jetblue.Models.DAO.Document;
import org.jetblue.jetblue.Models.DAO.Passenger;
import org.jetblue.jetblue.Models.ENUM.DocumentType;
import org.jetblue.jetblue.Repositories.DocumentRepo;
import org.jetblue.jetblue.Repositories.PassengerRepo;
import org.jetblue.jetblue.Service.DocumentService;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.jetblue.jetblue.Utils.DocumentUtils.createPassengerFolder;
import static org.jetblue.jetblue.Utils.DocumentUtils.deletePassengerFile;
import static org.jetblue.jetblue.Utils.PathEncoded.DecodeFilePath;
import static org.jetblue.jetblue.Utils.PathEncoded.EncodeFilePath;

@Service
@AllArgsConstructor
public class DocumentImpl implements DocumentService {
    private DocumentRepo documentRepo;
    private PassengerRepo passengerRepo;


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
