package fr.norsys.apidocument.document.services;

import fr.norsys.apidocument.auth.repositories.UserRepository;
import fr.norsys.apidocument.auth.security.AuthService;
import fr.norsys.apidocument.document.controllers.ShareRequest;
import fr.norsys.apidocument.document.exceptions.PermissionNotAllowedException;
import fr.norsys.apidocument.document.models.Document;
import fr.norsys.apidocument.document.models.DocumentSharePermission;
import fr.norsys.apidocument.document.models.MetaData;
import fr.norsys.apidocument.document.exceptions.SameHashValueException;
import fr.norsys.apidocument.document.models.PermissionType;
import fr.norsys.apidocument.document.repositories.DocShareRepository;
import fr.norsys.apidocument.document.repositories.DocumentRepository;
import fr.norsys.apidocument.document.repositories.MetaDataRepository;
import fr.norsys.apidocument.document.repositories.PermissionRepository;
import fr.norsys.apidocument.document.utils.DocumentSpecifications;
import fr.norsys.apidocument.document.utils.FileUploadUtil;
import fr.norsys.apidocument.document.utils.HashCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final MetaDataRepository metaDataRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    private final DocShareRepository shareRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    public void saveDocument(MultipartFile file, List<MetaData> metadata) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SameHashValueException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Long size = file.getSize();

        UUID documentUUID = FileUploadUtil.saveFile(fileName, file);

        Document document = Document.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType().split("/")[1])
                .user(userRepository.findByEmail(authService.getCurrentUsername()))
                .documentUUID(documentUUID)
                .build();

        documentRepository.save(document);

        MetaData sizeMetadata = MetaData.builder()
                .key("size")
                .value(size.toString())
                .document(document)
                .build();

        metaDataRepository.save(sizeMetadata);

        // Save the uploaded file to a temporary location
        Path tempFilePath = Files.createTempFile("uploadedFile", null);
        file.transferTo(tempFilePath.toFile());

        // Calculate HMAC for the uploaded file
        byte[] secretKey = "secret".getBytes();
        String hmacValue = HashCalculator.calculateHmacOfFile(tempFilePath.toFile(), secretKey);

        if (metaDataRepository.existsHashValue(hmacValue)) {
            throw new SameHashValueException("File already exists");
        }

        // Save HMAC value as metadata
        MetaData hmacMetadata = MetaData.builder()
                .key("hash")
                .value(hmacValue)
                .document(document)
                .build();

        metaDataRepository.save(hmacMetadata);

        Files.delete(tempFilePath);

        // Save other metadata provided in the list
        metadata.forEach(
                mt -> {
                    String key = mt.getKey();
                    String value = mt.getValue();

                    MetaData metaData = MetaData.builder()
                            .key(key)
                            .value(value)
                            .document(document)
                            .build();

                    metaDataRepository.save(metaData);
                }
        );
    }

    @Transactional
    public void deleteDocument(UUID documentUUID) throws Exception {
        if (documentRepository.findByDocumentUUID(documentUUID) == null) {
            throw new IOException("File not found");
        }

        if (documentRepository.isUserOwnerOfDocument(documentUUID, authService.getCurrentUsername()) == 1 || shareRepository.isUserHavePermission(authService.getCurrentUsername(), PermissionType.DELETE, documentUUID) == 1) {
            System.out.println("OW"+documentRepository.isUserOwnerOfDocument(documentUUID, authService.getCurrentUsername()));
            System.out.println("PR"+shareRepository.isUserHavePermission(authService.getCurrentUsername(), PermissionType.DELETE, documentUUID));
            documentRepository.deleteByDocumentUUID(documentUUID);
            FileUploadUtil.deleteFile(documentUUID);
        } else throw new PermissionNotAllowedException("You are not the owner of the document");


    }

    public Document getDocument(UUID documentUUID) {
        return documentRepository.findByDocumentUUID(documentUUID);
    }

    public List<Document> getDocumentByCriteria(String searchName) {
        Specification<Document> spec = DocumentSpecifications.findByTypeOrNameOrCreationDate(searchName);
        return documentRepository.findAll(spec);
    }

    @Transactional
    public void shareDocument(ShareRequest shareRequest) {
        DocumentSharePermission sharePermission = DocumentSharePermission.builder()
                .document(documentRepository.findByDocumentUUID(shareRequest.getDocumentUUID()))
                .user(userRepository.findByEmail(shareRequest.getEmail()))
                .permission(permissionRepository.findByType(shareRequest.getPermission()))
                .build();

        System.out.println(permissionRepository.findByType(shareRequest.getPermission()));

        shareRepository.save(sharePermission);
    }


    public List<DocumentSharePermission> getSharedDocuments() {
        return shareRepository.findSharedDocuments(authService.getCurrentUsername());
    }


    @Transactional
    public void updateDocument(UUID documentUUID,MultipartFile file, List<MetaData> metadata) throws Exception {
        if (documentRepository.findByDocumentUUID(documentUUID) == null) {
            throw new IOException("File not found");
        }

        if (documentRepository.isUserOwnerOfDocument(documentUUID, authService.getCurrentUsername()) == 1 || shareRepository.isUserHavePermission(authService.getCurrentUsername(), PermissionType.WRITE, documentUUID) == 1) {
            System.out.println("OW"+documentRepository.isUserOwnerOfDocument(documentUUID, authService.getCurrentUsername()));
            System.out.println("PR"+shareRepository.isUserHavePermission(authService.getCurrentUsername(), PermissionType.DELETE, documentUUID));

            FileUploadUtil.deleteFile(documentUUID);

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Long size = file.getSize();

            FileUploadUtil.saveFile(fileName, file);

            Document document = Document.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType().split("/")[1])
                    .user(userRepository.findByEmail(authService.getCurrentUsername()))
                    .documentUUID(documentUUID)
                    .id(documentRepository.findByDocumentUUID(documentUUID).getId())
                    .build();

            documentRepository.save(document);

            metaDataRepository.deleteByDocumentId(documentRepository.findByDocumentUUID(documentUUID).getId());

            MetaData sizeMetadata = MetaData.builder()
                    .key("size")
                    .value(size.toString())
                    .document(document)
                    .build();

            metaDataRepository.save(sizeMetadata);

            // Save the uploaded file to a temporary location
            Path tempFilePath = Files.createTempFile("uploadedFile", null);
            file.transferTo(tempFilePath.toFile());

            // Calculate HMAC for the uploaded file
            byte[] secretKey = "secret".getBytes();
            String hmacValue = HashCalculator.calculateHmacOfFile(tempFilePath.toFile(), secretKey);

            if (metaDataRepository.existsHashValue(hmacValue)) {
                throw new SameHashValueException("File already exists");
            }

            // Save HMAC value as metadata
            MetaData hmacMetadata = MetaData.builder()
                    .key("hash")
                    .value(hmacValue)
                    .document(document)
                    .build();

            metaDataRepository.save(hmacMetadata);

            Files.delete(tempFilePath);

            // Save other metadata provided in the list
            metadata.forEach(
                    mt -> {
                        String key = mt.getKey();
                        String value = mt.getValue();

                        MetaData metaData = MetaData.builder()
                                .key(key)
                                .value(value)
                                .document(document)
                                .build();

                        metaDataRepository.save(metaData);
                    }
            );
        } else throw new PermissionNotAllowedException("You do not own this document. Please request permission from the owner.");
    }

}
