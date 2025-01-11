package org.jetblue.jetblue.Mapper.FlightStatus;

import org.jetblue.jetblue.Models.DAO.FlightStatus;
import org.springframework.stereotype.Service;

@Service
public class FlightStatusMapper {
    public static FlightStatus toFlightStatus(FlightStatusRequest flightStatus) {
        return FlightStatus
                .builder()
                .status(flightStatus.status())
                .build();
    }

    public static FlightStatusResponse toFlightStatusResponse(FlightStatus flightStatus) {
        return FlightStatusResponse.builder().status(flightStatus.getStatus()).build();
    }
}
