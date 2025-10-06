package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "offer_baggage_item")
public class OfferBaggageItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "baggage_catalogue_id", nullable = false)
  private CatalogBaggage catalogBaggage; // Correction du type : pas une List, mais une seule instance

  @ManyToOne
  @JoinColumn(name = "flight_baggage_offer_id", nullable = false)
  private FlightBaggageOffer flightBaggageOffer;

  private int quantity;
}
