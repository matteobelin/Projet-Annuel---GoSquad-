package com.gosquad.presentation.controller;

import com.gosquad.presentation.DTO.FlightResponseDTO;
import com.gosquad.usecase.flights.FlightService;
import com.gosquad.domain.flights.FlightEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.client.HttpClientErrorException;

@RestController
public class FlightController {
    private final FlightService flightService;
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/flights")
    public ResponseEntity<List<FlightResponseDTO>> getFlights(
            @RequestParam String dep_iata,
            @RequestParam String arr_iata) {
        try {
            List<FlightEntity> flights = flightService.getFlights(dep_iata, arr_iata);
            List<FlightResponseDTO> response = flights.stream().map(f ->
                new FlightResponseDTO(
                    f.getFlightNumber(),
                    f.getDepartureIata(),
                    f.getArrivalIata(),
                    f.getFlightDate(),
                    f.getAirline(),
                    f.getStatus()
                )
            ).collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
} 