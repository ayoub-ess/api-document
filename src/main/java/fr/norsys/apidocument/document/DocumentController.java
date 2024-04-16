package fr.norsys.apidocument.document;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile multipartFile)
            throws IOException {


        documentService.saveDocument(multipartFile);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getfilles")
    public List<Document> getfilles(){
        return documentService.getAllDocuments();
    }
}
