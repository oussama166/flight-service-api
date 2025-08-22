package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Mapper.Document.DocumentResponse;
import org.jetblue.jetblue.Models.DAO.Document;
import org.jetblue.jetblue.Models.ENUM.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepo extends JpaRepository<Document, Integer> {
    List<Document> findByPassenger_Id(Long passengerId);

    List<Document> findByPassenger_IdAndType(Long passengerId, DocumentType type);
}
