package com.gosquad.presentation.DTO;

public record FlightResponseDTO(
    String flightNumber,
    String departureIata,
    String arrivalIata,
    String flightDate,
    String airline,
    String status
) {} 