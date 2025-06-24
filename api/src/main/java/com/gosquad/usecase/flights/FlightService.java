package com.gosquad.usecase.flights;

import com.gosquad.domain.flights.FlightEntity;
import java.util.List;

public interface FlightService {
    List<FlightEntity> getFlights(String depIata, String arrIata) throws Exception;
} 