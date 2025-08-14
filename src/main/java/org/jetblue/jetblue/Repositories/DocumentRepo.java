package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepo extends JpaRepository<Document, Integer> {
}
