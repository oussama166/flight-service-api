package org.jetblue.jetblue.Mapper.Document;

import org.jetblue.jetblue.Models.DAO.Document;
import org.springframework.stereotype.Component;

import static org.jetblue.jetblue.Utils.PathEncoded.DecodeFilePath;

@Component
public class DocumentMapper {

    public static DocumentResponse toDocResponse(Document doc) {
        if (doc == null) {
            return null;
        }
        return DocumentResponse.builder()
                .documentType(doc.getType().getDisplayName())
                .documentUrl(DecodeFilePath(doc.getPath()))
                .fullName(doc.getPassenger().getFirstName() + " " + doc.getPassenger().getLastName())
                .build();
    }

}
