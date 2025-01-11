package org.jetblue.jetblue.Mapper.Airport;

import org.jetblue.jetblue.Models.DAO.Airport;
import org.springframework.stereotype.Service;

@Service
public class AirportMapper {
    public static Airport toAirport(AirportRequest airport) {
        return Airport.builder()
                .code(airport.code())
                .name(airport.name())
                .location(airport.location())
                .latitude(airport.latitude())
                .longitude(airport.longitude())
                .build();
    }

    public static AirportResponse toAirportResponse(Airport airport) {
        return AirportResponse.builder()
                .code(airport.getCode())
                .name(airport.getName())
                .location(airport.getLocation())
                .latitude(airport.getLatitude())
                .longitude(airport.getLongitude())
                .build();
    }
}
