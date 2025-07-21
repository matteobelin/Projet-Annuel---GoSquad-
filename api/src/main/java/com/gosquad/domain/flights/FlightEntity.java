package com.gosquad.domain.flights;

import com.gosquad.domain.Entity;
import lombok.Getter;

@Getter
public class FlightEntity extends Entity {
    private final String flightNumber;
    private final String departureIata;
    private final String arrivalIata;
    private final String flightDate;
    private final String airline;
    private final String status;

    public FlightEntity(String flightNumber, String departureIata, String arrivalIata, String flightDate, String airline, String status) {
        this.flightNumber = flightNumber;
        this.departureIata = departureIata;
        this.arrivalIata = arrivalIata;
        this.flightDate = flightDate;
        this.airline = airline;
        this.status = status;
    }

}