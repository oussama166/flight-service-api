package org.jetblue.jetblue.Service.Implementation;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.FlightBaggageOffer.FlightBaggageOfferRes;
import org.jetblue.jetblue.Mapper.OfferBaggageItem.OfferBaggageItemReq;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.FlightBaggageOffer;
import org.jetblue.jetblue.Repositories.FlightBaggageOfferRepo;
import org.jetblue.jetblue.Repositories.FlightRepo;
import org.jetblue.jetblue.Service.FlightBaggageOfferService;
import org.jetblue.jetblue.Service.OfferBaggageItemService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightBaggageOfferImpl implements FlightBaggageOfferService {
  private final FlightBaggageOfferRepo flightBaggageOfferRepo;
  private final OfferBaggageItemService offerBaggageItemService;
  private FlightRepo flightRepo;

  @Override
  public void createNewOffer(
    String name,
    String description,
    java.math.BigDecimal price,
    String flight_number,
    List<@Valid OfferBaggageItemReq> items
  ) {
    // Validate input parameters
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty.");
    }
    if (description == null || description.isEmpty()) {
      throw new IllegalArgumentException(
        "Description cannot be null or empty."
      );
    }
    if (price == null || price.compareTo(java.math.BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Price must be greater than zero.");
    }
    if (flight_number == null || flight_number.isEmpty()) {
      throw new IllegalArgumentException(
        "Flight number cannot be null or empty."
      );
    }

    Flight flightRes = flightRepo
      .findOne(
        (root, query, criteriaBuilder) ->
          criteriaBuilder.equal(root.get("flightNumber"), flight_number)
      )
      .orElseThrow(
        () ->
          new RuntimeException(
            "Flight with number " + flight_number + " not found."
          )
      );
    // Create and save the new FlightBaggageOffer entity
    FlightBaggageOffer newOffer = FlightBaggageOffer
      .builder()
      .name(name)
      .description(description)
      .price(price.doubleValue())
      .flight(flightRes)
      .build();
    FlightBaggageOffer offerSaved = flightBaggageOfferRepo.save(newOffer);

    if (offerSaved == null) {
      throw new RuntimeException("Failed to create flight baggage offer.");
    }
    for (OfferBaggageItemReq item : items) {
      try {
        offerBaggageItemService.addNewItem(
          item.itemCode(),
          item.quantity(),
          offerSaved.getCodeOffer()
        );
      } catch (Exception e) {
        // Rollback the created offer if any item addition fails
        flightBaggageOfferRepo.delete(offerSaved);
        throw new RuntimeException(
          "Failed to add item: " +
          item.itemCode() +
          "\nMain cause root is: " +
          e.getMessage(),
          e
        );
      }
    }
  }

  @Override
  public List<FlightBaggageOfferRes> showFlightOffers(String flight_number) {
    if (flight_number == null || flight_number.isEmpty()) {
      throw new IllegalArgumentException(
        "Flight number cannot be null or empty."
      );
    }
    Flight flightRes = flightRepo
      .findOne(
        (root, query, criteriaBuilder) ->
          criteriaBuilder.equal(root.get("flightNumber"), flight_number)
      )
      .orElseThrow(
        () ->
          new RuntimeException(
            "Flight with number " + flight_number + " not found."
          )
      );
    List<FlightBaggageOfferRes> offers = flightBaggageOfferRepo
      .findAll(
        (root, query, criteriaBuilder) ->
          criteriaBuilder.equal(root.get("flight"), flightRes)
      )
      .stream()
      .map(
        offer ->
          new FlightBaggageOfferRes(
            offer.getId(),
            offer.getCodeOffer(),
            offer.getName(),
            offer.getDescription(),
            offer.getPrice()
          )
      )
      .toList();
    if (offers.isEmpty()) {
      System.out.println("No offers found for flight number: " + flight_number);
    } else {
      System.out.println("Offers for flight number " + flight_number + ":");
      return offers;
    }
    return null;
  }
}
