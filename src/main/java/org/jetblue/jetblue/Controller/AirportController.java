package org.jetblue.jetblue.Controller;

import org.jetblue.jetblue.Models.DAO.Airport;
import org.jetblue.jetblue.Service.AirportService;
import org.springframework.http.ResponseEntity;
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
            @RequestBody Airport airport
    ){
        try {
                Airport airportResponse =  airportService.createAirport(airport);

                if(airportResponse != null) {
                    return ResponseEntity.ok("Airport already exist !!!");
                }
                else {
                    return ResponseEntity.ok("Airport created successfully !!!");
                }
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred  at server level !!!");
        }
    }

    @PatchMapping(
            value = "/updateAirport/{code}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> updateAirport(
            @RequestBody Airport airport,
            @PathVariable String code
    ){
        Airport airportResponse = airportService.updateAirport(code, airport);

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
                return ResponseEntity.ok(airport);
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
        List<Airport> airports = airportService.getAllAirports();

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
