package org.jetblue.jetblue.Mapper.FlightStatus;

import jakarta.validation.constraints.NotNull;

public record FlightStatusRequest(
        @NotNull(message = "Status should be unique and not null !!!")
        String status
) {
}
