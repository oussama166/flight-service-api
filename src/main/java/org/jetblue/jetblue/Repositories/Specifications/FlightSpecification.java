package org.jetblue.jetblue.Repositories.Specifications;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.springframework.data.jpa.domain.Specification;

public final class FlightSpecification {

  private FlightSpecification() {
  }

  public static Specification<Flight> buildSpecification(
      LocalDateTime departureTime,
      LocalDateTime arrivalTime,
      String departureLocation,
      String departureCountry,
      String arrivalLocation,
      String arrivalCountry,
      String status) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (departureTime != null) {
        predicates.add(
            cb.greaterThanOrEqualTo(root.get("departureTime"), departureTime));
      }

      if (arrivalTime != null) {
        predicates.add(
            cb.lessThanOrEqualTo(root.get("arrivalTime"), arrivalTime));
      }

      if (departureLocation != null && !departureLocation.isBlank()) {
        predicates.add(
            cb.like(
                cb.upper(root.get("departure").get("location")),
                "%" + departureLocation.trim().toUpperCase() + "%"));
      }

      if (arrivalLocation != null && !arrivalLocation.isBlank()) {
        predicates.add(
            cb.like(
                cb.upper(root.get("arrival").get("location")),
                "%" + arrivalLocation.trim().toUpperCase() + "%"));
      }
      if (departureCountry != null && !departureCountry.isBlank()) {
        predicates.add(
            cb.like(
                cb.upper(root.get("departure").get("country")),
                "%" + departureCountry.trim().toUpperCase() + "%"));
      }
      if (arrivalCountry != null && !arrivalCountry.isBlank()) {
        predicates.add(
            cb.like(
                cb.upper(root.get("arrival").get("country")),
                "%" + arrivalCountry.trim().toUpperCase() + "%"));
      }

      if (status != null && !status.isBlank()) {
        predicates.add(
            cb.like(
                cb.upper(root.get("status").get("status")),
                "%" + status.trim().toUpperCase() + "%"));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
