package fr.norsys.apidocument.document.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;

public class HashCalculator {
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    public static String calculateHmacOfFile(File file, byte[] secretKey) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        byte[] buffer = new byte[8192]; // 8KB buffer size
        int bytesRead;
        Mac hmac = Mac.getInstance(HMAC_ALGORITHM);
        hmac.init(new SecretKeySpec(secretKey, HMAC_ALGORITHM));

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                hmac.update(buffer, 0, bytesRead);
            }
        }

        byte[] hmacBytes = hmac.doFinal();
        return convertBytesToHexString(hmacBytes);
    }

    private static String convertBytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}