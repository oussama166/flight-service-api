package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType; // Ajouté pour optimiser le chargement
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn; // Ajouté pour la relation ManyToOne
import jakarta.persistence.ManyToOne; // Changé de OneToMany à ManyToOne
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor; // Ajouté pour JPA/Hibernate
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor; // Ajouté pour JPA/Hibernate
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flight_baggage_offer")
public class FlightBaggageOffer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code_offer", nullable = false, unique = true)
  private String codeOffer;

  private String name;
  private String description;
  private double price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "flight_id", nullable = false)
  private Flight flight;

  @OneToMany(
    mappedBy = "flightBaggageOffer",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<OfferBaggageItem> offerItems;

  @OneToMany(
    mappedBy = "flightBaggageOffer",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<BookingBaggageFlight> bookingBaggageFlights;

  @ManyToOne(fetch = FetchType.LAZY)
  @PrePersist
  private void prePersist() {
    if (codeOffer == null || codeOffer.isEmpty()) {
      codeOffer = "OFFER-" + System.currentTimeMillis();
    }
  }

  @Override
  public String toString() {
    return (
      "FlightBaggageOffer{" +
      "id=" +
      id +
      ", codeOffer='" +
      codeOffer +
      '\'' +
      ", name='" +
      name +
      '\'' +
      ", description='" +
      description +
      '\'' +
      ", price=" +
      price
    );
  }
}
