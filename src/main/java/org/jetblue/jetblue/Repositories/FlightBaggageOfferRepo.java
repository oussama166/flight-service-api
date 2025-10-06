package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.FlightBaggageOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FlightBaggageOfferRepo
  extends
    JpaRepository<FlightBaggageOffer, Long>,
    JpaSpecificationExecutor<FlightBaggageOffer> {}
