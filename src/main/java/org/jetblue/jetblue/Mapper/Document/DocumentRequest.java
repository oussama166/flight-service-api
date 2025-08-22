package org.jetblue.jetblue.Mapper.Document;

import lombok.Builder;
import org.jetblue.jetblue.Models.ENUM.DocumentType;
import org.springframework.web.multipart.MultipartFile;


@Builder
public record DocumentRequest(
        String passengerId,
        DocumentType documentType,
        String documentUrl,
        MultipartFile documentFile,
        String documentFilePathLocally

) {


}
