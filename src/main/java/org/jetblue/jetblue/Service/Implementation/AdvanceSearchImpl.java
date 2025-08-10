package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Flight.FlightMapper;
import org.jetblue.jetblue.Mapper.Flight.FlightResponse;
import org.jetblue.jetblue.Mapper.Search.SearchRequest;
import org.jetblue.jetblue.Repositories.FlightRepo;
import org.jetblue.jetblue.Service.AdvanceSearchService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdvanceSearchImpl implements AdvanceSearchService {

    private final FlightRepo flightRepo;


    @Override
    public List<FlightResponse> searchFlights(
            SearchRequest searchRequest
    ) {
        return flightRepo.findFlightsAdvanced(
                searchRequest.departureTime(),
                searchRequest.arrivalTime(),
                searchRequest.origin(),
                searchRequest.destination(),
                searchRequest.flightStatus()
        ).stream().map(FlightMapper::toFlightResponse).collect(Collectors.toList());
    }



}
