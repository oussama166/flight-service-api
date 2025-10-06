package org.jetblue.jetblue.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.CatalogBaggage.CatalogBaggageReq;
import org.jetblue.jetblue.Service.CatalogBaggageService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CatalogBaggageController {
  private CatalogBaggageService catalogBaggageService;

  @PostMapping("/catalog-baggage")
  public ResponseEntity<?> setBaggage(
    @Valid @RequestBody CatalogBaggageReq entity
  ) {
    try {
      String catalogBaggage = catalogBaggageService.createCatalogBaggage(
        entity
      );
      return ResponseEntity.ok(catalogBaggage);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/catalog-baggages")
  public ResponseEntity<?> getAllCatalogBaggage() {
    return ResponseEntity.ok(catalogBaggageService.getAllCatalogBaggage());
  }

  @PostMapping("/catalog-baggage/add-airline/{airlineName}/{baggageCode}")
  public String addAirlineToBaggage(
    @PathVariable String airlineName,
    @PathVariable String baggageCode
  ) {
    catalogBaggageService.addAirlineToCatalogBaggage(airlineName, baggageCode);
    return "Airline added to baggage successfully";
  }
}
