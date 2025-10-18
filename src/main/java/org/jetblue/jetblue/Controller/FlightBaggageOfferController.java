package org.jetblue.jetblue.Controller;

import java.util.List;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONArray;
import org.jetblue.jetblue.Mapper.FlightBaggageOffer.FlightBaggageOfferReq;
import org.jetblue.jetblue.Mapper.FlightBaggageOffer.FlightBaggageOfferRes;
import org.jetblue.jetblue.Service.FlightBaggageOfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FlightBaggageOfferController {
  private final FlightBaggageOfferService flightBaggageOfferService;

  @PostMapping("/newOffer")
  public ResponseEntity<?> newOfferRequest(
    @RequestBody FlightBaggageOfferReq request
  ) {
    try {
      flightBaggageOfferService.createNewOffer(
        request.name(),
        request.description(),
        request.price(),
        request.flight_number(),
        request.items()
      );
      return ResponseEntity.ok("Offer created successfully.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (RuntimeException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @GetMapping("flightOffers")
  public ResponseEntity<?> getMethodName(@RequestParam String flight_number) {
    try {
      List<FlightBaggageOfferRes> showFlightOffers = flightBaggageOfferService.showFlightOffers(
        flight_number
      );
      return ResponseEntity.ok(showFlightOffers);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (RuntimeException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }
}
