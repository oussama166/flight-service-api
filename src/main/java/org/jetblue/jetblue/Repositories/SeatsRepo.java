package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SeatsRepo extends JpaRepository<Seat, Long> {

    Optional<Seat> findBySeatNumber(int seatId);
    @Query(
            "SELECT st FROM Seat st where st.seatNumber = ?1 AND st.airPlane.name LIKE %?2% "
    )
    Optional<Seat> findBySeatNumberAndAirPlane(int seatNumber, String airplaneName);


}
