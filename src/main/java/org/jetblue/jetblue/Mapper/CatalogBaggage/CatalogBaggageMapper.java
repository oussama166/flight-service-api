package org.jetblue.jetblue.Mapper.CatalogBaggage;

import org.jetblue.jetblue.Models.DAO.CatalogBaggage;

public class CatalogBaggageMapper {

  public static CatalogBaggageRes toCatalogBaggageRes(CatalogBaggage cat) {
    return CatalogBaggageRes
      .builder()
      .code(cat.getCode())
      .name(cat.getName())
      .description(cat.getDescription())
      .baggageType(cat.getBaggageType().getDisplayName())
      .maxWeightKg(cat.getMaxWeightKg())
      .dimensions(cat.getDimensions())
      .airlineNames(
        cat.getAirline().stream().map(a -> a.getAirlineName()).toList()
      )
      .build();
  }
}
