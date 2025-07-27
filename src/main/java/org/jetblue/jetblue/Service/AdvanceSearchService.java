package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.Flight.FlightResponse;
import org.jetblue.jetblue.Mapper.Search.SearchRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AdvanceSearchService {
    /**
     * This method is used to search for flights based on the provided search request.
     * @param request The search request containing the criteria for searching flights.
     * @return A list of FlightResponse objects that match the search criteria.
     * */
    List<FlightResponse> searchFlights(
            SearchRequest request
    );


}
