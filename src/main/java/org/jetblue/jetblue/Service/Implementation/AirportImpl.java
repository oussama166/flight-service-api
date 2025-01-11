package org.jetblue.jetblue.Service.Implementation;

import org.jetblue.jetblue.Mapper.Airport.AirportMapper;
import org.jetblue.jetblue.Mapper.Airport.AirportResponse;
import org.jetblue.jetblue.Models.DAO.Airport;
import org.jetblue.jetblue.Repositories.AirportRepo;
import org.jetblue.jetblue.Service.AirportService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AirportImpl implements AirportService {

    // Integration
    private final AirportRepo airportRepo;

    public AirportImpl(AirportRepo airportRepo) {
        this.airportRepo = airportRepo;
    }

    @Override
    public Airport createAirport(Airport airport) {
        // check if there is any airport with the same code in the same country
        Airport airportInfo = airportRepo.findByCode(airport.getCode()).orElse(null);

        if (airportInfo == null) {
            airportRepo.save(airport);
            return airport;
        }
        return null;
    }

    @Override
    public Airport updateAirport(String code, Airport airport) {
        Airport airportInfo = airportRepo.findByCode(code).orElse(null);

        if (airportInfo == null) {
            createAirport(airport);
            return null;
        } else {
            airportInfo.setName(airport.getName());
            airportInfo.setCode(airport.getCode());
            airportInfo.setLocation(airport.getLocation());
            airportInfo.setLongitude(airport.getLongitude());
            airportInfo.setLatitude(airport.getLatitude());
            airportRepo.save(airportInfo);
            return airport;
        }

    }

    @Override
    public Airport getAirport(String code) {
        return airportRepo.findByCodeOrLocation(code).orElse(null);
    }

    @Override
    public List<AirportResponse> getAllAirports() {
        return airportRepo.findAll().stream().map(AirportMapper::toAirportResponse).collect(Collectors.toList());
    }

    @Override
    public boolean deleteAirport(String code) {
        // try to find the airport and delete the airport
        Airport airport = airportRepo.findByCode(code).orElse(null);
        if (airport == null) {
            return false;
        }
        airportRepo.delete(airport);
        return true;
    }
}
