package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Airline.AirlineMapper;
import org.jetblue.jetblue.Mapper.Airline.AirlineRequest;
import org.jetblue.jetblue.Mapper.Airline.AirlineResponse;
import org.jetblue.jetblue.Models.DAO.Airline;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Repositories.AirlineRepo;
import org.jetblue.jetblue.Service.AirlineService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AirlineImpl implements AirlineService {

    // inject
    private final AirlineRepo airlineRepo;


    @Override
    public AirlineResponse setAirline(Airline airline) {
        // find the airline in database
        Airline airlineResp = airlineRepo.findByAirlineName(airline.getAirlineName()).orElse(null);
        if (airlineResp == null) {
            airlineRepo.save(airline);
            return AirlineMapper.toAirlineResponse(airline);
        }
        //! - Refactor this code : Raise stackoverflow issue
        return null;
    }

    @Override
    public AirlineResponse getAirline(String airlineName) {
        if (airlineName == null) return null;
        if (airlineName.isBlank()) return null;
        Airline airline = airlineRepo
                .findByAirlineCode(airlineName)
                .orElse(null);
        if (airline == null) {
            return null;
        }
        return AirlineMapper.toAirlineResponse(airline);
    }

    @Override
    public AirlineResponse updateAirline(String airlineName, AirlineRequest airline) {
        Airline airlineResp = airlineRepo
                .findByAirlineCode(airlineName)
                .orElse(null);
        if (airlineResp == null) {
            setAirline(AirlineMapper.toAirline(airline));
            return null;
        } else {
            airlineResp.setAirlineName(airline.airlineName());
            airlineResp.setAirlineCode(airline.airlineCode());
            airlineResp.setAirlineUrl(airline.airlineUrl());
            airlineResp.setAirlineLogoLink(airline.airlineLogoLink());
            airlineRepo.save(airlineResp);
            return AirlineMapper.toAirlineResponse(airlineResp);
        }
    }

    @Override
    public Boolean deleteAirline(String airlineName) {
        Airline airlineResp = airlineRepo.findByAirlineCode(airlineName).orElse(null);
        if (airlineResp != null) {
            airlineRepo.delete(airlineResp);
            return true;
        }
        return false;

    }

    @Override
    public Set<Flight> getAllAirlineFlights(LocalDateTime from, LocalDateTime to, String airlineName) {
        return airlineRepo.findByDepartureTimeAndArrivalTimeAndAirlineName(
                from,
                to,
                airlineName
        );
    }

    @Override
    public List<Airline> getAllAirlines() {
        return airlineRepo.findAll();
    }
}
