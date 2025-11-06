package org.jetblue.jetblue.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
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
@Tag(
  name = "FlightBaggageOffer",
  description = "Endpoints to manage baggage offers for flights"
)
public class FlightBaggageOfferController {
  private final FlightBaggageOfferService flightBaggageOfferService;

  @PostMapping("/newOffer")
  @Operation(
    summary = "Create new baggage offer",
    description = "Create a new baggage offer for a flight using the supplied request body"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Offer created successfully"
      ),
      @ApiResponse(responseCode = "400", description = "Bad request"),
      @ApiResponse(
        responseCode = "404",
        description = "Related entity not found"
      ),
    }
  )
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

  @Operation(
    summary = "Get baggage offers for a flight",
    description = "Retrieve all baggage offers available for the given flight number"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "List of offers returned"
      ),
      @ApiResponse(responseCode = "400", description = "Bad request"),
      @ApiResponse(responseCode = "404", description = "Flight not found"),
    }
  )
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
