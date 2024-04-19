package fr.norsys.apidocument.document.repositories;

import fr.norsys.apidocument.document.models.DocumentSharePermission;
import fr.norsys.apidocument.document.models.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DocShareRepository extends JpaRepository<DocumentSharePermission, Long>{

    @Query("select count(dsp) from DocumentSharePermission dsp join dsp.user u join dsp.permission p where u.email = :email and p.type = :permission and dsp.document.documentUUID = :documentId")
    int isUserHavePermission(String email, PermissionType permission, UUID documentId);

    @Query("select dsp from DocumentSharePermission dsp join Document d on d.id = dsp.document.id join dsp.user u where u.email = :email")
    List<DocumentSharePermission> findSharedDocuments(String email);


}