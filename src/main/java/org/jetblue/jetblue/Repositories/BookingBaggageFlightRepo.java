package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.BookingBaggageFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingBaggageFlightRepo
  extends
    JpaRepository<BookingBaggageFlight, Long>,
    JpaSpecificationExecutor<BookingBaggageFlight> {}
