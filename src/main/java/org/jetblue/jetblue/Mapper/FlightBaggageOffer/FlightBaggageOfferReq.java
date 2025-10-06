package org.jetblue.jetblue.Mapper.FlightBaggageOffer;

import java.math.BigDecimal;
import java.util.List;

import org.jetblue.jetblue.Mapper.OfferBaggageItem.OfferBaggageItemReq;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record FlightBaggageOfferReq(
  @NotNull
  String name,
  @NotNull
  String description,
  @DecimalMin("1.0")
  BigDecimal price,
  @NotNull
  String flight_number,
  List<OfferBaggageItemReq> items
) {
}