package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetblue.jetblue.Models.DAO.CatalogBaggage;
import org.jetblue.jetblue.Models.DAO.FlightBaggageOffer;
import org.jetblue.jetblue.Models.DAO.OfferBaggageItem;
import org.jetblue.jetblue.Repositories.CatalogBaggageRepo;
import org.jetblue.jetblue.Repositories.FlightBaggageOfferRepo;
import org.jetblue.jetblue.Repositories.OfferBaggageItemRepo;
import org.jetblue.jetblue.Service.OfferBaggageItemService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class OfferBaggageItemImpl implements OfferBaggageItemService {
  private final OfferBaggageItemRepo offerBaggageItemRepo;
  private final CatalogBaggageRepo catalogBaggageRepo;
  private final FlightBaggageOfferRepo flightBaggageOfferRepo;

  @Override
  public void addNewItem(String itemCode, int quantity, String offerCode) {
    // check if all the params are valid
    if (itemCode == null || itemCode.isEmpty()) {
      throw new IllegalArgumentException("Item code cannot be null or empty.");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than zero.");
    }
    if (offerCode == null || offerCode.isEmpty()) {
      throw new IllegalArgumentException("Offer code cannot be null or empty.");
    }

    // Check if the catalog item exists
    CatalogBaggage catBag = catalogBaggageRepo
      .findOne(
        (root, query, criteriaBuilder) ->
          criteriaBuilder.equal(root.get("code"), itemCode)
      )
      .orElseThrow(
        () ->
          new RuntimeException(
            "Catalog item with code " + itemCode + " not found."
          )
      );
    log.info("Catalog item found: " + catBag.getCode());
    FlightBaggageOffer FlBgOf = flightBaggageOfferRepo
      .findOne(
        (root, query, criteriaBuilder) ->
          criteriaBuilder.equal(root.get("codeOffer"), offerCode)
      )
      .orElseThrow(
        () ->
          new RuntimeException(
            "Flight baggage offer with code " + offerCode + " not found."
          )
      );
    log.info("Flight baggage offer found: " + FlBgOf.getCodeOffer());
    OfferBaggageItem newOfferItem = OfferBaggageItem
      .builder()
      .quantity(quantity)
      .catalogBaggage(catBag)
      .flightBaggageOffer(FlBgOf)
      .build();
    log.info(
      "New offer item created: " + newOfferItem.getCatalogBaggage().getCode()
    );
    // Save the new offer item
    offerBaggageItemRepo.save(newOfferItem);
  }

  @Override
  public void removeItem(String itemCode, String offerCode) {}

  @Override
  public void updateItemQuantity(
    String itemCode,
    int newQuantity,
    String offerCode
  ) {}

  @Override
  public java.util.List<org.jetblue.jetblue.Mapper.OfferBaggageItem.OfferBaggageItemRes> getItemsByOfferCode(
    String offerCode
  ) {
    return null;
  }
}
