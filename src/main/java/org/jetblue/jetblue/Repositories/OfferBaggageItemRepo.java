package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.OfferBaggageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OfferBaggageItemRepo
  extends
    JpaRepository<OfferBaggageItem, Long>,
    JpaSpecificationExecutor<OfferBaggageItem> {}
