package fr.norsys.apidocument.document.repositories;

import fr.norsys.apidocument.document.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, Long> , JpaSpecificationExecutor<Document> {
    void deleteByDocumentUUID(UUID documentUUID);
    Document findByDocumentUUID(UUID documentUUID);


}
