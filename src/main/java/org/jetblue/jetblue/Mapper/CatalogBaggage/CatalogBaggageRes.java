package org.jetblue.jetblue.Mapper.CatalogBaggage;

import java.util.List;

import lombok.Builder;

@Builder
public record CatalogBaggageRes(
    String code,
    String name,
    String description,
    String baggageType,
    double maxWeightKg,
    String dimensions,
    List<String> airlineNames
) {
}