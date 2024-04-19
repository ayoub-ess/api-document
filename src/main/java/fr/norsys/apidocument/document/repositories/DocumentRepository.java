package fr.norsys.apidocument.document.repositories;

import fr.norsys.apidocument.document.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, Long> , JpaSpecificationExecutor<Document> {
    void deleteByDocumentUUID(UUID documentUUID);
    Document findByDocumentUUID(UUID documentUUID);
    @Query("select d from DocumentSharePermission dsp join Document d on d.id = dsp.document.id join dsp.user u where u.email = :email")
    List<Document> findSharedDocuments(String email);

    @Query("SELECT COUNT(d) FROM Document d JOIN d.user u WHERE d.documentUUID = :documentUUID AND u.email = :email")
    int isUserOwnerOfDocument(UUID documentUUID,String email);

}
