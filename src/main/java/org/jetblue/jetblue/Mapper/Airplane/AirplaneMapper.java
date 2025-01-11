package org.jetblue.jetblue.Mapper.Airplane;

import org.jetblue.jetblue.Mapper.Airline.AirlineResponse;
import org.jetblue.jetblue.Models.DAO.Airplane;
import org.springframework.stereotype.Service;

@Service
public class AirplaneMapper {

    public  Airplane toAirplane(AirplaneRequest airplaneRequest) {
        return Airplane.builder()
                .name(airplaneRequest.name())
                .airplaneImageUrl(airplaneRequest.airplaneImageUrl())
                .maxSeat(airplaneRequest.maxSeat())
                .build();
    }

    public AirplaneResponse toAirplaneResponse(Airplane airplane) {
        return AirplaneResponse.builder()
                .name(airplane.getName())
                .airplaneImageUrl(airplane.getAirplaneImageUrl())
                .maxSeat(airplane.getMaxSeat())
                .build();
    }

}
