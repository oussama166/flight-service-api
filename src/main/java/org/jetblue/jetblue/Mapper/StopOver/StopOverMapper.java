package org.jetblue.jetblue.Mapper.StopOver;

import org.jetblue.jetblue.Models.DAO.StopOver;
import org.springframework.stereotype.Service;

@Service
public class StopOverMapper {
    public StopOverResponse toStopOverResponse(StopOver stopOver) {
        return StopOverResponse.builder()
                .stopOrder(stopOver.getStopOrder())
                .arrivalTime(stopOver.getArrivalTime())
                .departureTime(stopOver.getDepartureTime())
                .flightNumber(stopOver.getFlight().getFlightNumber())
                .airportName(stopOver.getAirport().getName())
                .airportCode(stopOver.getAirport().getCode())
                .stopDuration(stopOver.getFormattedStopDuration())
                .build();
    }
}
