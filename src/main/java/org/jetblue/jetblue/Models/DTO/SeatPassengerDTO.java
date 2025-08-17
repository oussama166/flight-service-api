package org.jetblue.jetblue.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetblue.jetblue.Mapper.Document.DocumentRequest;
import org.jetblue.jetblue.Models.DAO.Document;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatPassengerDTO {
    private long seatNumber;
    private String firstName;
    private String lastName;
    private String middleName = "";
    private String passportNumber;
    private LocalDate passportExpirationDate;
    private LocalDate birthDate;
    private int age;
    private boolean isUnaccompanied;
    private Set<DocumentRequest> documents;
}
