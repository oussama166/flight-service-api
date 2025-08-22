package org.jetblue.jetblue.Events;

import lombok.Getter;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.springframework.context.ApplicationEvent;

@Getter
public class SeatDynamizedPriceEvent extends ApplicationEvent {
    private final Flight flight;

    public SeatDynamizedPriceEvent(Object source, Flight flightId) {
        super(source);
        this.flight = flightId;
    }

}
