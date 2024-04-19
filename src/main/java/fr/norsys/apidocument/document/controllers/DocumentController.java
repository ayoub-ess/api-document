package fr.norsys.apidocument.document.controllers;

import fr.norsys.apidocument.document.utils.FileDownloadUtil;
import fr.norsys.apidocument.document.models.Document;
import fr.norsys.apidocument.document.models.MetaData;
import fr.norsys.apidocument.document.services.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/document")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity uploadFile(@RequestPart("file") MultipartFile file, @RequestPart("metadata") List<MetaData> metadata)
            throws Exception {

        documentService.saveDocument(file,metadata);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{documentUUID}")
    public ResponseEntity deleteFile(
            @PathVariable("documentUUID") UUID documentUUID)
            throws Exception {

        documentService.deleteDocument(documentUUID);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{documentUUID}")
    public ResponseEntity<Document> getDocument(
            @PathVariable("documentUUID") UUID documentUUID) {
        Document document = documentService.getDocument(documentUUID);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Document>> getDocumentsByCriteria(@RequestParam(defaultValue = "",required = false) String searchName) {

        List<Document> documents = documentService.getDocumentByCriteria(searchName);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/downloadFile/{documentUUID}")
    public ResponseEntity<?> downloadFile(@PathVariable("documentUUID") UUID documentUUID) {
        FileDownloadUtil downloadUtil = new FileDownloadUtil();

        Resource resource = null;
        try {
            resource = downloadUtil.getFileAsResource(documentUUID);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    @PostMapping("/share")
    public ResponseEntity<?> shareDocument(@RequestBody ShareRequest shareRequest) {
        documentService.shareDocument(shareRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/shared")
    public ResponseEntity<List<Document>> getSharedDocuments() {
        List<Document> documents = documentService.getSharedDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity updateDocument(@RequestParam UUID documentUUID ,@RequestPart("file") MultipartFile file, @RequestPart("metadata") List<MetaData> metadata) throws Exception {
        documentService.updateDocument(documentUUID,file,metadata);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
