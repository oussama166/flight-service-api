package org.jetblue.jetblue.Mapper.FlightBaggageOffer;

public record FlightBaggageOfferRes(
  Long id,
  String codeOffer,
  String name,
  String description,
  double price
) {
}