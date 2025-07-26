package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.Flight.FlightResponse;
import org.jetblue.jetblue.Mapper.Search.SearchRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AdvanceSearchService {
    List<FlightResponse> searchFlights(
            SearchRequest request
    );


}
