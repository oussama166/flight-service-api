package org.jetblue.jetblue.Mapper.Airplane;

import org.jetblue.jetblue.Mapper.Airline.AirlineResponse;
import org.jetblue.jetblue.Models.DAO.Airplane;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirplaneMapper {

    public  static Airplane toAirplane(AirplaneRequest airplaneRequest) {
        return Airplane.builder()
                .name(airplaneRequest.name())
                .airplaneImageUrl(airplaneRequest.airplaneImageUrl())
                .maxSeat(airplaneRequest.maxSeat())
                .build();
    }

    public static List<Airplane> toAirplanes(List<AirplaneRequest> airplaneRequests) {
        return airplaneRequests.stream()
                .map(AirplaneMapper::toAirplane)
                .collect(Collectors.toList());
    }

    public static AirplaneResponse toAirplaneResponse(Airplane airplane) {
        return AirplaneResponse.builder()
                .name(airplane.getName())
                .airplaneImageUrl(airplane.getAirplaneImageUrl())
                .maxSeat(airplane.getMaxSeat())
                .build();
    }

    public static List<AirplaneResponse> toAirplaneResponses(List<Airplane> newAirplanes) {
        return newAirplanes.stream()
                .map(AirplaneMapper::toAirplaneResponse)
                .collect(Collectors.toList());
    }
}
