package org.jetblue.jetblue.Mapper.Airline;

import org.jetblue.jetblue.Models.DAO.Airline;
import org.springframework.stereotype.Service;

@Service
public class AirlineMapper {

    public static Airline toAirline(AirlineRequest airlineRequest) {
        return Airline.builder()
                .airlineCode(airlineRequest.airlineCode())
                .airlineName(airlineRequest.airlineName())
                .airlineUrl(airlineRequest.airlineUrl())
                .airlineLogoLink(airlineRequest.airlineLogoLink())
                .colFormation(airlineRequest.colFormation())
                .rowFormation(airlineRequest.rowFormation())
                .build();
    }

    public static AirlineResponse toAirlineResponse(Airline airline) {
        return AirlineResponse.builder()
                .airlineCode(airline.getAirlineCode())
                .airlineName(airline.getAirlineName())
                .airlineUrl(airline.getAirlineUrl())
                .airlineLogoLink(airline.getAirlineLogoLink())
                .colFormation(airline.getColFormation())
                .rowFormation(airline.getRowFormation())
                .build();

    }
}
