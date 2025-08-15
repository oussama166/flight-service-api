package org.jetblue.jetblue.Utils;

import org.jetblue.jetblue.Models.DAO.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

}
