package org.jetblue.jetblue.Events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class SeatReservedEvent extends ApplicationEvent {
    private final Long flightId;
    private final String seatClass;

    public SeatReservedEvent(Object source, Long flightId, String seatClass) {
        super(source);
        this.flightId = flightId;
        this.seatClass = seatClass;
    }

}