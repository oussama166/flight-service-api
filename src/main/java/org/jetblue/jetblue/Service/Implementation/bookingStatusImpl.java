package org.jetblue.jetblue.Service.Implementation;

import org.jetblue.jetblue.Mapper.BookingStatus.BookingStatusRequest;
import org.jetblue.jetblue.Mapper.BookingStatus.BookingStatusesMapper;
import org.jetblue.jetblue.Models.DAO.BookingStatus;
import org.jetblue.jetblue.Repositories.BookingStatusRepo;
import org.jetblue.jetblue.Service.BookingStatusService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class bookingStatusImpl implements BookingStatusService {

    // Inject the dependence
    private final BookingStatusRepo bookingStatusRepo;

    public bookingStatusImpl(BookingStatusRepo bookingStatusRepo) {
        this.bookingStatusRepo = bookingStatusRepo;
    }

    @Override
    public BookingStatus setBookingStatus(BookingStatusRequest bookingStatus) {
        // trying to find the booking status
        BookingStatus bookingStats = bookingStatusRepo.findByStatus(bookingStatus.status()).orElse(null);

        if (bookingStats != null) {
            throw new IllegalStateException("BookingRepo status not found");
        }
        BookingStatus st = BookingStatus.builder()
                .status(bookingStatus.status())
                .build();
        bookingStatusRepo.save(st);
        return st;

    }

    @Override
    public BookingStatus setBookingStatus(List<BookingStatusRequest> bookingStatuses) {

        if (bookingStatuses.isEmpty()) {
            throw new IllegalArgumentException("bookingStatuses cannot be null or empty");
        }

        for (BookingStatusRequest bookingStatus : bookingStatuses) {
            setBookingStatus(bookingStatus);
        }
        return BookingStatusesMapper.toBookingStatus(bookingStatuses.get(0));
    }

    @Override
    public BookingStatus getBookingStatus(String bookingName) {
        return bookingStatusRepo.findByStatus(bookingName).orElseThrow(() -> new IllegalStateException("BookingRepo status not found"));
    }

    @Override
    public List<BookingStatus> getAllBookingStatuses() {
        return bookingStatusRepo.findAll();
    }

    @Override
    public BookingStatus updateBookingStatus(BookingStatus bookingStatus) {
        // try to get the booking status
        BookingStatus bookingStat = bookingStatusRepo.findByStatus(bookingStatus.getStatus()).orElseThrow(
                () -> new DataIntegrityViolationException("BookingRepo status not found")
        );

        bookingStatus.setStatus(bookingStatus.getStatus());

        return bookingStatusRepo.save(bookingStat);
    }

    @Override
    public BookingStatus deleteBookingStatus(String bookingName) {
        BookingStatus bookingStatus = bookingStatusRepo.findByStatus(bookingName).orElseThrow(
                () -> new DataIntegrityViolationException("BookingRepo status not found")
        );
        bookingStatusRepo.delete(bookingStatus);
        return bookingStatus;
    }
}
