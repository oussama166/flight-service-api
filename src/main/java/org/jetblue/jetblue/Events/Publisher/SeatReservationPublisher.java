package org.jetblue.jetblue.Events.Publisher;

import org.jetblue.jetblue.Events.SeatReservedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SeatReservationPublisher {
    private final ApplicationEventPublisher publisher;

    public SeatReservationPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishSeatReserved(Long flightId, String seatClass) {
        SeatReservedEvent event = new SeatReservedEvent(this, flightId, seatClass);
        publisher.publishEvent(event);
    }
}
