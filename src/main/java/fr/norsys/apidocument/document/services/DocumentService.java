package fr.norsys.apidocument.document.services;

import fr.norsys.apidocument.document.models.Document;
import fr.norsys.apidocument.document.models.MetaData;
import fr.norsys.apidocument.document.exceptions.SameHashValueException;
import fr.norsys.apidocument.document.repositories.DocumentRepository;
import fr.norsys.apidocument.document.repositories.MetaDataRepository;
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


    @Transactional
    public void saveDocument(MultipartFile file, List<MetaData> metadata) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SameHashValueException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Long size = file.getSize();



        UUID documentUUID = FileUploadUtil.saveFile(fileName, file);

        Document document = Document.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType().split("/")[1])
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
    public void deleteDocument(UUID documentUUID) throws IOException {
        documentRepository.deleteByDocumentUUID(documentUUID);
        FileUploadUtil.deleteFile(documentUUID);
    }

    public Document getDocument(UUID documentUUID) {
        return documentRepository.findByDocumentUUID(documentUUID);
    }

    public List<Document> getDocumentByCriteria(String searchName) {
        Specification<Document> spec = DocumentSpecifications.findByTypeOrNameOrCreationDate(searchName);
        return documentRepository.findAll(spec);
    }
}
