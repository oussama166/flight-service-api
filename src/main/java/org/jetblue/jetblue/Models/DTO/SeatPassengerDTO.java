package org.jetblue.jetblue.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Document;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatPassengerDTO {
    private int seatNumber;
    private String firstName;
    private String lastName;
    private String middleName = "";
    private int age;
    private boolean isUnaccompanied;
    private Set<Document> documents ;
}
