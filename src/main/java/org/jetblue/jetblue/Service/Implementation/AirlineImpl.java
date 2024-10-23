package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
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
    public Airline setAirline(Airline airline) {
        // find the airline in database
        Airline airlineResp = airlineRepo.findByAirlineName(airline.getAirlineName()).orElse(null);
        if (airlineResp == null) {
            airlineRepo.save(airline);
            return airline;
        } else {
            updateAirline(airline.getAirlineName(), airline);
            return null;
        }
    }

    @Override
    public Airline getAirline(String airlineName) {
        return airlineRepo
                .findByAirlineCode(airlineName)
                .orElse(null);
    }

    @Override
    public Airline updateAirline(String airlineName, Airline airline) {
        Airline airlineResp = airlineRepo
                .findByAirlineCode(airlineName)
                .orElse(null);
        if (airlineResp == null) {
            setAirline(airline);
            return new Airline();
        } else {
            airlineResp.setAirlineName(airline.getAirlineName());
            airlineResp.setAirlineCode(airline.getAirlineCode());
            airlineResp.setAirlineUrl(airline.getAirlineUrl());
            airlineResp.setAirlineLogoLink(airline.getAirlineLogoLink());
            airlineRepo.save(airlineResp);
            return airlineResp;
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
