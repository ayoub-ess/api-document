package fr.norsys.apidocument.document.utils;

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

    public static void deleteFile(UUID documentUUID) throws IOException {
        Path uploadPath = Paths.get("Files-Upload");
        Path filePath = uploadPath.resolve(documentUUID.toString());

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new IOException("Could not delete file with UUID: " + documentUUID, e);
        }
    }
}