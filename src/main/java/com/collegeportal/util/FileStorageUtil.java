package com.collegeportal.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import jakarta.servlet.http.Part;

public class FileStorageUtil {

    /**
     * Validates and saves an uploaded photo to disk.
     * Returns the generated filename (not the full path) to store in the DB.
     * Throws IllegalArgumentException with a user-facing message if validation fails.
     */
    public static String savePhoto(Part filePart) throws IOException {

        if (filePart == null || filePart.getSize() == 0) {
            throw new IllegalArgumentException("No file was selected.");
        }

        if (filePart.getSize() > Constants.MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File is too large. Maximum size is 2MB.");
        }

        // Read the first few bytes to detect the REAL file type, not the
        // extension or the browser-supplied Content-Type header, either of
        // which can be trivially faked by renaming a file.
        String detectedType;
        try (InputStream in = filePart.getInputStream()) {
            byte[] header = in.readNBytes(12);
            detectedType = detectImageType(header);
        }

        if (detectedType == null || !Constants.ALLOWED_IMAGE_TYPES.contains(detectedType)) {
            throw new IllegalArgumentException("Only JPEG, PNG, or WebP images are allowed.");
        }

        // Generate a random, safe filename — never trust the original
        // filename (path traversal risk, collisions, special characters).
        String extension = switch (detectedType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".bin";
        };
        String newFilename = UUID.randomUUID().toString() + extension;

        Path uploadDir = Paths.get(Constants.UPLOAD_DIR);
        Files.createDirectories(uploadDir); // safe no-op if it already exists

        Path targetPath = uploadDir.resolve(newFilename);

        try (InputStream in = filePart.getInputStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        return newFilename;
    }

    /**
     * Deletes a previously stored photo from disk, given its stored filename.
     * Safe to call with null/blank (does nothing) or a file that no longer exists.
     */
    public static void deletePhoto(String filename) {
        if (filename == null || filename.isBlank()) {
            return;
        }
        try {
            Path target = Paths.get(Constants.UPLOAD_DIR).resolve(filename);
            Files.deleteIfExists(target);
        } catch (IOException e) {
            // Log and move on — a failed cleanup of an old file shouldn't
            // block the user's upload/update from succeeding.
            e.printStackTrace();
        }
    }

    /**
     * Detects real image type by inspecting file signature ("magic bytes"),
     * not by trusting the filename extension or client-supplied MIME type.
     */
    private static String detectImageType(byte[] header) {
        if (header.length >= 3
                && (header[0] & 0xFF) == 0xFF
                && (header[1] & 0xFF) == 0xD8
                && (header[2] & 0xFF) == 0xFF) {
            return "image/jpeg";
        }
        if (header.length >= 8
                && (header[0] & 0xFF) == 0x89 && header[1] == 'P' && header[2] == 'N' && header[3] == 'G'
                && header[4] == 0x0D && header[5] == 0x0A && header[6] == 0x1A && header[7] == 0x0A) {
            return "image/png";
        }
        if (header.length >= 12
                && header[0] == 'R' && header[1] == 'I' && header[2] == 'F' && header[3] == 'F'
                && header[8] == 'W' && header[9] == 'E' && header[10] == 'B' && header[11] == 'P') {
            return "image/webp";
        }
        return null;
    }
}