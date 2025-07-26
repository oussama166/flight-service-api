package org.jetblue.jetblue.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Search.SearchRequest;
import org.jetblue.jetblue.Service.AdvanceSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/v1")
@AllArgsConstructor
public class AdvanceSearchController {

    private final AdvanceSearchService advanceSearchService;

    @GetMapping(
            name="searchFlights",
            value="/searchFlights",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> searchFlights(@RequestBody @Valid SearchRequest query) {
        try {
            // Call the service to get the search results
            var searchResults = advanceSearchService.searchFlights(query);
            // Return the search results with HTTP status 200 (OK)
            return ResponseEntity.ok(searchResults);
        } catch (Exception e) {
            // If there's an error, return a 500 (Internal Server Error) with the error message
            return ResponseEntity.status(500).body("Server error occurred: " + e.getMessage());
        }
    }

}
