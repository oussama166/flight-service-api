package org.jetblue.jetblue.Events.Listener;

import org.jetblue.jetblue.Events.SeatCancelledEvent;
import org.jetblue.jetblue.Events.SeatReservedEvent;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Repositories.FlightRepo;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SeatEventListener {

    private final FlightRepo flightRepository;

    public SeatEventListener(FlightRepo flightRepository) {
        this.flightRepository = flightRepository;
    }

    @EventListener
    public void handleSeatReserved(SeatReservedEvent event) {
        Flight flight = flightRepository.findById(event.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        switch (event.getSeatClass().toUpperCase()) {
            case "ECONOMY" -> flight.setThirdClassReserve(flight.getThirdClassReserve() + 1);
            case "BUSINESS" -> flight.setSecondClassReserve(flight.getSecondClassReserve() + 1);
            case "FIRST" -> flight.setFirstClassReserve(flight.getFirstClassReserve() + 1);
        }

        flightRepository.save(flight);
    }

    @EventListener
    public void handleSeatCancelled(SeatCancelledEvent event) {
        Flight flight = flightRepository.findById(event.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        switch (event.getSeatClass().toUpperCase()) {
            case "ECONOMY" -> flight.setThirdClassReserve(flight.getThirdClassReserve() - 1);
            case "BUSINESS" -> flight.setSecondClassReserve(flight.getSecondClassReserve() - 1);
            case "FIRST" -> flight.setFirstClassReserve(flight.getFirstClassReserve() - 1);
        }

        flightRepository.save(flight);
    }
}

