package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking_baggage_flight")
public class BookingBaggageFlight {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "booking_passenger_id", nullable = false)
  private BookingPassenger bookingPassenger;

  @ManyToOne
  @JoinColumn(name = "flight_baggage_offer_id", nullable = false)
  private FlightBaggageOffer flightBaggageOffer;

  private int quantity;

  private double priceAtPurchase;
}
