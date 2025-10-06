package org.jetblue.jetblue.Service;

import java.util.List;
import org.jetblue.jetblue.Mapper.CatalogBaggage.CatalogBaggageReq;
import org.jetblue.jetblue.Mapper.CatalogBaggage.CatalogBaggageRes;

public interface CatalogBaggageService {
  String createCatalogBaggage(CatalogBaggageReq catalogBaggageReq);
  List<CatalogBaggageRes> getAllCatalogBaggage();
  void addAirlineToCatalogBaggage(String airlineName, String baggageCode);
  void updateCatalogBaggage(
    String baggageCode,
    CatalogBaggageReq catalogBaggageReq
  );
  void deleteCatalogBaggage(String baggageCode);
  void getCatalogBaggageById();
}
