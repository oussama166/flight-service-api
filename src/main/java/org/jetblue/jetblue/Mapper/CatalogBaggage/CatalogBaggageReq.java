package org.jetblue.jetblue.Mapper.CatalogBaggage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CatalogBaggageReq(
    @Pattern(
        regexp = "^(Carry-On|Checked|Oversize|Special Item)$",
        message = "Baggage type must be one of the following: Carry-On, Checked, Oversize, Special"
    )
    String baggageType,
    @NotNull
    String baggageName,
    @NotNull
    String baggageDescription,
    @NotNull
    String dimensions,
    @NotNull
    @Min(2)
    double baggageMaxWeightKg,
    String airlineName
) {
}