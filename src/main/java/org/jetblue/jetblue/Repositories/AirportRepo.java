package org.jetblue.jetblue.Repositories;

import java.util.Optional;
import org.jetblue.jetblue.Models.DAO.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AirportRepo extends JpaRepository<Airport, Long> {
  @Query(
    value = "select air from Airport air where air.location = :locationOrCode or air.code = :locationOrCode"
  )
  Optional<Airport> findByCodeOrLocation(
    @Param("locationOrCode") String locationOrCode
  );

  Optional<Airport> findByCode(String code);

  Optional<Airport> findByCodeAndLatitudeAndLongitude(
    String code,
    double latitude,
    double longitude
  );
}
