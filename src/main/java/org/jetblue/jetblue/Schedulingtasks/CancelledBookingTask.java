package org.jetblue.jetblue.Schedulingtasks;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;

@Component
@AllArgsConstructor
public class CancelledBookingTask {
    private static final Logger log = LoggerFactory.getLogger(CancelledBookingTask.class);
    private static final String TASK_NAME = "Cancelled Booking Task";
    private final BookingService bookingService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // every day at midnight, this method will be executed
    // @Scheduled(cron = "0 0 0 * * *")
    // every 10 minutes, this method will be executed
    @Scheduled(cron = "0 0 0 * * *")
    public void cancelBooking() {
        bookingService.getAllBookings().forEach(booking -> {
            // check if the time of creation over the 24 hours
            if (LocalDateTime
                    .now()
                    .isAfter(
                            booking.getCreateTime().plusHours(24)
                    )
            ) {
                log.info("Cancelling booking with ID: {}", booking.getBookingId());
                boolean cancelBooking = bookingService.cancelBooking(booking.getBookingId());
                if (cancelBooking) {
                    log.info("Booking with ID: {} has been cancelled successfully", booking.getBookingId());
                } else {
                    log.error("Failed to cancel booking with ID: {}", booking.getBookingId());
                }
            } else {
                log.info("Booking with ID: {} is still valid, not cancelling", booking.getBookingId());
            }
        });
    }
}

