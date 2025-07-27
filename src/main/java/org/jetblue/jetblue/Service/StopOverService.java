package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.StopOver.StopOverRequest;
import org.jetblue.jetblue.Mapper.StopOver.StopOverResponse;

import java.util.List;

public interface StopOverService {
    /// Retrieves the stopover details for a given flight ID.
    ///
    /// @param flightNumber the ID of the flight for which to retrieve stopover details
    /// @return a <b>String</b> containing the stopover details
    List<StopOverResponse> getStopOverDetails(String flightNumber);


    /// Adds a stopover to a flight.
    ///
    /// @param flightId        the ID of the flight to which the stopover will be added
    /// @param airportId       the ID of the airport where the stopover will occur
    /// @param stopOverDetails the details of the stopover to be added
    void addStopOver(String flightId, String airportId, StopOverRequest stopOverDetails);

    /// Reorders a stopover for a flight.
    ///
    /// @param flightId the ID of the flight for which the stopover will be reordered
    /// @param oldIndex the current index of the stopover to be reordered
    /// @param newIndex the new index to which the stopover will be moved
    void reorderStopOver(String flightId, int oldIndex, int newIndex);

    /// Deletes a stopover for a flight.
    ///
    /// @param flightId  the ID of the flight from which the stopover will be deleted
    /// @param stopOrderIndex the index of the stopover to be deleted
    void deleteStopOver(String flightId,int stopOrderIndex);
}
