package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Models.DAO.Airplane;

import java.util.List;

public interface AirplaneService {


    Airplane create(Airplane airplane);

    Airplane update(String airplaneName,Airplane airplane);

    Airplane getAirplane(String airplaneName);

    List<Airplane> getAllAirplanes();

    Airplane delete(String airplaneName);
}
