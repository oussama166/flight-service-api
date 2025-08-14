package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.Flight.FlightResponse;
import org.jetblue.jetblue.Mapper.Search.SearchRequest;
import org.jetblue.jetblue.Models.Entity.FlightItinerary;
import org.springframework.stereotype.Service;

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

    List<FlightItinerary> searchItinerary(String origin, String destination, LocalDateTime departureDate);


}
