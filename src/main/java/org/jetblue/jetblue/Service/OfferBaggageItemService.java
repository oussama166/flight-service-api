package org.jetblue.jetblue.Service;

import java.util.List;
import org.jetblue.jetblue.Mapper.OfferBaggageItem.OfferBaggageItemRes;

public interface OfferBaggageItemService {
  void addNewItem(String itemCode, int quantity, String offerCode);
  void removeItem(String itemCode, String offerCode);
  void updateItemQuantity(String itemCode, int newQuantity, String offerCode);
  List<OfferBaggageItemRes> getItemsByOfferCode(String offerCode);
}
