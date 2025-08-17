package org.jetblue.jetblue.Utils;

import org.jetblue.jetblue.Models.DAO.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static java.time.LocalDate.now;

public class DocumentUtils {

    private final static String DOCUMENTS_PATH = "Uploads/Documents/";

    public static String createPassengerFolder(String passengerName , String documentType, MultipartFile file) throws IOException {
        // Base folder for documents
        File passengerFolder = new File(DOCUMENTS_PATH);
        if (!passengerFolder.exists()) {
            passengerFolder.mkdirs();
        }

        // Passenger-specific folder
        File documentFolder = new File(passengerFolder, passengerName + "/");
        if (!documentFolder.exists()) {
            documentFolder.mkdirs();
        }

        // Extract file extension
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Current date in yyyyMMdd format
        String dateStr = now().toString(); // yyyy-MM-dd by default

        // Build the new filename
        String newFileName = passengerName + "_" + documentType + "_" + dateStr + extension;

        // Save file with the new name
        Path documentPath = Paths.get(documentFolder.getAbsolutePath(), newFileName);
        Files.write(documentPath, file.getBytes());

        // Here you might return a Document object (adjust as per your model)
        return documentPath.toAbsolutePath().toString();
    }
    public static String createPassengerFolderFromPath(String passengerName, String documentType, String filePath) throws IOException {
        // Base folder for documents
        File passengerFolder = new File(DOCUMENTS_PATH);
        if (!passengerFolder.exists()) {
            passengerFolder.mkdirs();
        }

        // Passenger-specific folder
        File documentFolder = new File(passengerFolder, passengerName + "/");
        if (!documentFolder.exists()) {
            documentFolder.mkdirs();
        }

        // Extract extension from the filePath
        File sourceFile = new File(filePath);
        if (!sourceFile.exists()) {
            throw new IllegalArgumentException("File does not exist at path: " + filePath);
        }

        String originalFilename = sourceFile.getName();
        String extension = "";
        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Current date (yyyyMMdd)
        String dateStr = java.time.LocalDate.now().toString();

        // Build the new filename
        String newFileName = passengerName + "_" + documentType + "_" + dateStr + extension;

        // Destination path
        Path documentPath = Paths.get(documentFolder.getAbsolutePath(), newFileName);

        // Copy file to destination
        Files.copy(sourceFile.toPath(), documentPath, StandardCopyOption.REPLACE_EXISTING);

        return documentPath.toAbsolutePath().toString();
    }

    public static boolean deletePassengerFile(String fileUrl) {
        // Construct the file path
        Path filePath = Paths.get(fileUrl);


        // Check if the file exists
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
                return true;
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getDocument(String fileUrl) throws IOException {
        File sourceFile = new File(fileUrl);

        if (!sourceFile.exists()) {
            throw new IllegalArgumentException("File does not exist at path: " + fileUrl);
        }

        // Ensure upload dir exists
        Path uploadPath = Path.of(DOCUMENTS_PATH);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Copy file to uploads folder
        Path destination = uploadPath.resolve(sourceFile.getName());
        Files.copy(sourceFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

        return destination.toAbsolutePath().toString(); // re


    }





}
