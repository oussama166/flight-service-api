package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.FlightStatus;
import org.jetblue.jetblue.Repositories.FlightStatusRepo;
import org.jetblue.jetblue.Service.FlightStatusService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightStatusImpl implements FlightStatusService {

    private FlightStatusRepo flightStatusRepo;

    @Override
    public FlightStatus setFlightStatus(FlightStatus flightStatus) {
        // trying to find the flight status
        FlightStatus flightStatusResp = flightStatusRepo.findByStatus(flightStatus.getStatus()).orElse(null);
        if (flightStatusResp == null) {
            flightStatusRepo.save(flightStatus);
            return flightStatus;
        } else {
            return null;
        }
    }


    @Override
    public boolean setListFlightStatus(List<String> status) {
        if(status.isEmpty()) return false;
        for (String stat : status) {
            FlightStatus flightStatus = flightStatusRepo.findByStatus(stat).orElse(null);
            if (flightStatus == null) {
                flightStatus = new FlightStatus();
                flightStatus.setStatus(stat);
                flightStatusRepo.save(flightStatus);
            }
        }
        return true;
    }

    @Override
    public FlightStatus getFlightStatus(String status) {
        return flightStatusRepo.findByStatus(status).orElse(null);
    }


    @Override
    public List<FlightStatus> getAllFlightStatus() {
        return flightStatusRepo.findAll().stream()
                .sorted(Comparator.comparing(FlightStatus::getId))
                .toList();
    }

    @Override
    public FlightStatus updateFlightStatus(String status, FlightStatus flightStatus) {
        // trying to find the flight status
        FlightStatus flightStatusResp = flightStatusRepo.findByStatus(status).orElse(null);
        if (flightStatusResp != null) {
            flightStatusResp.setStatus(status);
            return flightStatus;
        }
        return null;
    }

    @Override
    public Boolean deleteFlightStatus(String status) {
        // trying to find the flight status
        FlightStatus flightStatusResp = flightStatusRepo.findByStatus(status).orElse(null);

        if (flightStatusResp != null) {
            flightStatusRepo.delete(flightStatusResp);
            return true;
        }
        return false;
    }
}
