package org.jetblue.jetblue.Mapper.Flight;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.jetblue.jetblue.Models.ENUM.PricingType;

import java.time.LocalDateTime;

public record FlightRequest(
        @NotNull(message = "Departure Time of the flight can not be null !!!")
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        @NotNull(message = "the base price of the flight can not be null !!!")
        @DecimalMin(value = "1", message = "Can not set price to flight less then 1")
        double price,
        @NotNull(message = "Max Seats for the flight can not be empty or null")
        @DecimalMin(value = "10", message = "Min value of seats accepted is 10 seats !!!")
        int maxSeats,
        @NotNull(message = "Departure of the flight must be inserted !!!")
        @NotEmpty
        String departure,
        @NotNull(message = "Arrival of the flight must be inserted !!!")
        @NotEmpty
        String arrival,
        @NotNull(message = "Arrival of the flight must be inserted !!!")
        @NotEmpty
        String airline,
        @NotNull(message = "Insert the type of the airplane !!!")
        @NotEmpty
        String airplane,
        @NotNull(message = "Max first class seats for the flight can not be empty or null")
        @DecimalMin(value = "0", message = "Min value of First class seats accepted is 0 seats !!!")
        int maxFirst,
        @NotNull(message = "Max second class seats for the flight can not be empty or null")
        @DecimalMin(value = "0", message = "Min value of second class seats accepted is 0 seats !!!")
        int maxSecond,
        @NotNull(message = "Max third class seats for the flight can not be empty or null")
        @DecimalMin(value = "0", message = "Min value of third class seats accepted is 0 seats !!!")
        int maxThird,
        @NotNull(message = "Please insert the flight status to be shown for client !!!")
        String flightStatus,
        @JsonProperty(defaultValue = "DYNAMIC")
        @Pattern(regexp = "FIXED|DYNAMIC|FLEXIBLE|DISCOUNTED|PREMIUM",
                message = "Invalid pricing type, must be one of FIXED, DYNAMIC, FLEXIBLE, DISCOUNTED, PREMIUM")
        String pricingType

) {
    public FlightRequest {
        if (pricingType == null || pricingType.isEmpty()) {
            pricingType = PricingType.DYNAMIC.name();
        }
    }
}
