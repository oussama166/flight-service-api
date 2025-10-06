package org.jetblue.jetblue.Service;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import org.jetblue.jetblue.Mapper.OfferBaggageItem.OfferBaggageItemReq;

public interface FlightBaggageOfferService {
  void createNewOffer(
    String name,
    String description,
    BigDecimal price,
    String flight_number,
    List<@Valid OfferBaggageItemReq> items
  );
  void showFlightOffers(String flight_number);
}
