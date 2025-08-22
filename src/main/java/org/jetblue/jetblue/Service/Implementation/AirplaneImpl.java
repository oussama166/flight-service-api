package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Airplane.AirplaneMapper;
import org.jetblue.jetblue.Mapper.Airplane.AirplaneResponse;
import org.jetblue.jetblue.Models.DAO.Airplane;
import org.jetblue.jetblue.Repositories.AirplaneRepo;
import org.jetblue.jetblue.Service.AirplaneService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AirplaneImpl implements AirplaneService {

    // Injection

    private final AirplaneRepo airplaneRepo;


    @Override
    public Airplane create(Airplane airplane) {
        // trying to find the airplane
        Airplane airplaneResp = airplaneRepo.findByName(airplane.getName()).orElse(null);
        if (airplaneResp != null) {
            return null;
        }
        airplaneRepo.save(airplane);
        return airplane;
    }

    @Override
    public List<Airplane> createAll(List<Airplane> airplanes) {
        for (Airplane airplane : airplanes) {
            // trying to find the airplane
            Airplane airplaneResp = airplaneRepo.findByName(airplane.getName()).orElse(null);
            if (airplaneResp != null) {
                continue;
            }
            airplaneRepo.save(airplane);
        }
        return airplanes.stream().
                filter(airplane -> airplaneRepo.findByName(airplane.getName()).isPresent())
                .toList();
    }

    @Override
    public Airplane update(String airplaneName, Airplane airplane) {
        // trying to find the airplane
        Airplane airplaneResp = airplaneRepo.findByName(airplaneName).orElse(null);
        if (airplaneResp != null) {
            airplaneResp.setName(airplane.getName() != null ? airplane.getName() : airplaneResp.getName());
            airplaneResp.setMaxSeat(airplane.getMaxSeat() > 0 ? airplane.getMaxSeat() : airplaneResp.getMaxSeat());
            airplaneResp.setSeats(airplane.getSeats() != null ? airplane.getSeats() : airplaneResp.getSeats());
            airplaneResp.setAirplaneImageUrl(airplane.getAirplaneImageUrl() != null ? airplane.getAirplaneImageUrl() : airplaneResp.getAirplaneImageUrl());
            airplaneRepo.save(airplaneResp);
        }
        return null;
    }


    @Override
    public Airplane getAirplane(String airplaneName) {
        return airplaneRepo.findByName(airplaneName).orElse(null);
    }

    @Override
    public List<AirplaneResponse> getAllAirplanes() {
        return airplaneRepo.findAll().stream().map(AirplaneMapper::toAirplaneResponse).toList();
    }

    @Override
    public Airplane delete(String airplaneName) {
        // finding the airplane
        Airplane airplaneResp = airplaneRepo.findByName(airplaneName).orElse(null);
        if (airplaneResp == null) {
            return null;
        }
        airplaneRepo.delete(airplaneResp);
        return airplaneResp;
    }
}
