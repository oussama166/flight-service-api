package org.jetblue.jetblue.Mapper.OfferBaggageItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OfferBaggageItemReq(
  @NotNull
  String itemCode,
  @NotNull
  @Min(1)
  int quantity,
  String offerCode
) {
}