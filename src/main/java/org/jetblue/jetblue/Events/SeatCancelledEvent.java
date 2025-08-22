package org.jetblue.jetblue.Events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SeatCancelledEvent extends ApplicationEvent {
    private final Long flightId;
    private final String seatClass;

    public SeatCancelledEvent(Object source, Long flightId, String seatClass) {
        super(source);
        this.flightId = flightId;
        this.seatClass = seatClass;
    }

}
