package org.jetblue.jetblue.Mapper.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetblue.jetblue.Models.ENUM.DocumentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;


@Builder
public record DocumentRequest(
        String passengerId,
        DocumentType documentType,
        String documentUrl,
        MultipartFile documentFile

) {


}
