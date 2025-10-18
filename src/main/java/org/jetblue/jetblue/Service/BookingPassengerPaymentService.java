package org.jetblue.jetblue.Service;

public interface BookingPassengerPaymentService {
  void addBookingPassengerPayment(
    String username,
    String bookingId,
    Long passengerId,
    String paymentId
  );
  void updateBookingPassengerPaymentTotalAmount(
    String username,
    String bookingId,
    Long passengerId,
    double amount
  );
}
