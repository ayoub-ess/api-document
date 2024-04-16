package fr.norsys.apidocument.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final MetaDataRepository metaDataRepository;



    @Transactional
    public void saveDocument(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Long size = file.getSize();

        UUID documentUUID = FileUploadUtil.saveFile(fileName, file);


        /*metadata*/



        Document document = Document.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .documentUUID(documentUUID)
                .build();

        documentRepository.save(document);

        MetaData sizeMetadata = MetaData.builder()
                .key("size")
                .value(size.toString())
                .document(document)
                .build();

        MetaData sizeMetadata2 = MetaData.builder()
                .key("size2")
                .value("dddd")
                .document(document)
                .build();

        metaDataRepository.save(sizeMetadata);
        metaDataRepository.save(sizeMetadata2);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }
}
