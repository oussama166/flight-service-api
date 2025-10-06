package org.jetblue.jetblue.Service.Implementation;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Airline.AirlineMapper;
import org.jetblue.jetblue.Mapper.CatalogBaggage.CatalogBaggageMapper;
import org.jetblue.jetblue.Mapper.CatalogBaggage.CatalogBaggageReq;
import org.jetblue.jetblue.Mapper.CatalogBaggage.CatalogBaggageRes;
import org.jetblue.jetblue.Models.DAO.Airline;
import org.jetblue.jetblue.Models.DAO.CatalogBaggage;
import org.jetblue.jetblue.Models.ENUM.BaggageType;
import org.jetblue.jetblue.Repositories.AirlineRepo;
import org.jetblue.jetblue.Repositories.CatalogBaggageRepo;
import org.jetblue.jetblue.Service.CatalogBaggageService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CatalogBaggageImpl implements CatalogBaggageService {
  private CatalogBaggageRepo catalogBaggageRepo;
  private AirlineMapper airlineMapper;
  private AirlineRepo airlineRepo;

  @Override
  public String createCatalogBaggage(CatalogBaggageReq catalogBaggageReq) {
    // check if airline exist and if catalogBaggage exist
    boolean exists = catalogBaggageRepo.exists(
      (root, query, builder) ->
        builder.equal(root.get("name"), catalogBaggageReq.baggageName())
    );

    if (exists) {
      throw new IllegalArgumentException(
        "CatalogBaggage with name " +
        catalogBaggageReq.baggageName() +
        " already exists."
      );
    }
    // create catalogBaggage
    CatalogBaggage catalogBaggage = CatalogBaggage
      .builder()
      .name(catalogBaggageReq.baggageName())
      .description(catalogBaggageReq.baggageDescription())
      .baggageType(BaggageType.fromDisplayName(catalogBaggageReq.baggageType()))
      .maxWeightKg(catalogBaggageReq.baggageMaxWeightKg())
      .dimensions(catalogBaggageReq.dimensions())
      .build();
    CatalogBaggage savedCata = catalogBaggageRepo.save(catalogBaggage);
    return (
      "CatalogBaggage created successfully with id: " + savedCata.getCode()
    );
  }

  @Override
  public List<CatalogBaggageRes> getAllCatalogBaggage() {
    return catalogBaggageRepo
      .findAll()
      .stream()
      .map(CatalogBaggageMapper::toCatalogBaggageRes)
      .toList();
  }

  @Override
  public void addAirlineToCatalogBaggage(
    String airlineName,
    String baggageCode
  ) {
    // Vérifier que le baggage existe
    CatalogBaggage baggage = catalogBaggageRepo
      .findOne(
        (root, query, builder) -> builder.equal(root.get("code"), baggageCode)
      )
      .orElseThrow(
        () ->
          new IllegalArgumentException(
            "CatalogBaggage with code " + baggageCode + " does not exist."
          )
      );

    Airline airline = airlineRepo
      .findByAirlineName(airlineName)
      .orElseThrow(
        () ->
          new IllegalArgumentException(
            "Airline with name " + airlineName + " does not exist."
          )
      );

    if (baggage.getAirline().contains(airline)) {
      throw new IllegalArgumentException(
        "Airline " +
        airlineName +
        " is already associated with CatalogBaggage " +
        baggageCode
      );
    }

    airline.setCatalogBaggage(baggage);

    baggage.getAirline().add(airline);

    airlineRepo.save(airline);
  }

  @Override
  public void updateCatalogBaggage(
    String baggageCode,
    CatalogBaggageReq catalogBaggageReq
  ) {
    Optional<CatalogBaggage> optionalBaggage = catalogBaggageRepo.findOne(
      (root, query, builder) -> builder.equal(root.get("code"), baggageCode)
    );

    if (optionalBaggage.isEmpty()) {
      throw new IllegalArgumentException(
        "CatalogBaggage with code " + baggageCode + " does not exist."
      );
    }

    CatalogBaggage baggageToUpdate = optionalBaggage.get();

    // Update fields
    baggageToUpdate.setName(catalogBaggageReq.baggageName());
    baggageToUpdate.setDescription(catalogBaggageReq.baggageDescription());
    baggageToUpdate.setBaggageType(
      BaggageType.fromDisplayName(catalogBaggageReq.baggageType())
    );
    baggageToUpdate.setMaxWeightKg(catalogBaggageReq.baggageMaxWeightKg());
    baggageToUpdate.setDimensions(catalogBaggageReq.dimensions());

    catalogBaggageRepo.save(baggageToUpdate);
  }

  @Override
  public void deleteCatalogBaggage(String baggageCode) {
    Optional<CatalogBaggage> optionalBaggage = catalogBaggageRepo.findOne(
      (root, query, builder) -> builder.equal(root.get("code"), baggageCode)
    );

    if (optionalBaggage.isEmpty()) {
      throw new IllegalArgumentException(
        "CatalogBaggage with code " + baggageCode + " does not exist."
      );
    }

    catalogBaggageRepo.delete(optionalBaggage.get());
  }

  @Override
  public void getCatalogBaggageById() {}
}
