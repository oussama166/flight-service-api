package org.jetblue.jetblue.Mapper.Airline;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Airline;
import org.jetblue.jetblue.Repositories.AirlineRepo;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AirlineMapper {
  private AirlineRepo airlineRepo;

  public static Airline toAirline(AirlineRequest airlineRequest) {
    return Airline
      .builder()
      .airlineCode(airlineRequest.airlineCode())
      .airlineName(airlineRequest.airlineName())
      .airlineUrl(airlineRequest.airlineUrl())
      .airlineLogoLink(airlineRequest.airlineLogoLink())
      .colFormation(airlineRequest.colFormation())
      .rowFormation(airlineRequest.rowFormation())
      .build();
  }

  public static AirlineResponse toAirlineResponse(Airline airline) {
    return AirlineResponse
      .builder()
      .airlineCode(airline.getAirlineCode())
      .airlineName(airline.getAirlineName())
      .airlineUrl(airline.getAirlineUrl())
      .airlineLogoLink(airline.getAirlineLogoLink())
      .colFormation(airline.getColFormation())
      .rowFormation(airline.getRowFormation())
      .build();
  }

  public Airline toAirlineResponseByName(String airlineName) {
    return airlineRepo.findByAirlineName(airlineName).orElse(null);
  }
}
