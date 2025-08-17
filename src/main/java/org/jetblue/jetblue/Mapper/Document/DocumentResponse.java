package org.jetblue.jetblue.Mapper.Document;

import lombok.Builder;

@Builder
public record DocumentResponse(
    String documentType,
    String documentUrl,
    String fullName

) {
}
