package org.jetblue.jetblue.Controller;

import jakarta.validation.Valid;
import org.jetblue.jetblue.Mapper.Airport.AirportMapper;
import org.jetblue.jetblue.Mapper.Airport.AirportRequest;
import org.jetblue.jetblue.Mapper.Airport.AirportResponse;
import org.jetblue.jetblue.Models.DAO.Airport;
import org.jetblue.jetblue.Service.AirportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AirportController {

    //  Integration
    private final AirportService airportService;

    // Constructor
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @PostMapping(value = "/setAirport",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> setAirport(
            @RequestBody AirportRequest airport
    ){
        try {
                Airport airportResponse =  airportService.createAirport(AirportMapper.toAirport(airport));

                if(airportResponse != null) {
                    return ResponseEntity.ok("Airport created successfully !!!");
                }
                else {
                    return ResponseEntity.ok("Airport already exist !!!");
                }
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred  at server level !!!");
        }
    }
    @PostMapping(
            value="/setAirports",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> setAirports(
            @RequestBody List<AirportRequest> airports
    ){
        try {
            List<Airport> airportResponse =  airportService.createAirports(airports);

            if(!airportResponse.isEmpty()) {
                return ResponseEntity.ok(airportResponse);
            }
            else {
                return ResponseEntity.ok("Airport already exist !!!");
            }
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred  at server level !!!");
        }
    }

    //!- Refactor this to could accept only the field or fields wanted changes
    @PatchMapping(
            value = "/updateAirport/{code}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> updateAirport(
            @RequestBody @Valid AirportRequest airport,
            @PathVariable String code
    ){
        Airport airportResponse = airportService.updateAirport(code, AirportMapper.toAirport(airport));

        try {
            if(airportResponse != null) {
                return ResponseEntity.ok("Airport updated successfully !!!");
            }
            else {
                return ResponseEntity.ok().body("Airport not found !!!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred  at server level !!!");
        }

    }

    @GetMapping(
           value = "/getAirport/{code}",
            produces = "application/json"
    )
    public ResponseEntity<?> getAirport(@PathVariable String code){
        Airport airport = airportService.getAirport(code);
        try {
            if(airport != null) {
                return ResponseEntity.ok(AirportMapper.toAirportResponse(airport));
            }
            else {
                return ResponseEntity.badRequest().body("Airport not found !!!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred  at server level !!!");
        }
    }

    @GetMapping(
            value ="/getAirports",
            produces = "application/json"
    )
    public ResponseEntity<?> getAllAirports(){
        List<AirportResponse> airports = airportService.getAllAirports();

        return ResponseEntity.ok(airports);
    }


    @DeleteMapping(
            value = "/deleteAirport/{code}"
    )
    public ResponseEntity<?> deleteAirport(@PathVariable String code){
        boolean deleted = airportService.deleteAirport(code);
        if(deleted) {
            return ResponseEntity.ok("Airport deleted successfully !!!");
        }
        else {
            return ResponseEntity.badRequest().body("Airport not found !!!");
        }
    }
}
