package fr.norsys.apidocument.document;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.*;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

public class FileUploadUtil {
    public static UUID saveFile(String fileName, MultipartFile multipartFile)
            throws IOException {
        Path uploadPath = Paths.get("Files-Upload");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        UUID documentUUID = UUID.randomUUID();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(documentUUID + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return documentUUID;
    }
}