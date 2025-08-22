package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.Flight.FlightResponse;
import org.jetblue.jetblue.Mapper.Search.SearchRequest;
import org.jetblue.jetblue.Models.Entity.FlightItinerary;

import java.time.LocalDateTime;
import java.util.List;

public interface AdvanceSearchService {
    /**
     * This method is used to search for flights based on the provided search request.
     *
     * @param request The search request containing the criteria for searching flights.
     * @return A list of FlightResponse objects that match the search criteria.
     */
    List<FlightResponse> searchFlights(
            SearchRequest request
    );

    /**
     * This method is used to search for flight itineraries based on the origin, destination, and departure date.
     *
     * @param origin The origin airport code or location.
     * @param destination The destination airport code or location.
     * @param departureDate The date and time of departure.
     * @return A list of FlightItinerary objects that match the search criteria.
     */
    List<FlightItinerary> searchItinerary(String origin, String destination, LocalDateTime departureDate);


}
