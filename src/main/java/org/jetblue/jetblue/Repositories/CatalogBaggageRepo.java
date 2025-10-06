package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.CatalogBaggage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CatalogBaggageRepo
  extends
    JpaRepository<CatalogBaggage, Long>,
    JpaSpecificationExecutor<CatalogBaggage> {}
