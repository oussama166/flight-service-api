package org.jetblue.jetblue.Mapper.StopOver;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record StopOverRequest(
        @NotNull
        int stopOrder,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @NotNull
        LocalDateTime arrivalTime,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @NotNull
        LocalDateTime departureTime

) {
}
