package com.gosquad.usecase.flights.impl;

import com.gosquad.domain.flights.FlightEntity;
import com.gosquad.usecase.flights.FlightService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import io.github.cdimascio.dotenv.Dotenv;

@Service
public class FlightServiceImpl implements FlightService {
    private static final String BASE_URL = "http://api.aviationstack.com/v1/flights";
    private static final String API_KEY;
    static {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("AVIATIONSTACK_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("[FlightServiceImpl] AVIATIONSTACK_API_KEY is missing! Vérifiez votre fichier .env à la racine du projet.");
        }
        API_KEY = apiKey;
    }

    @Override
    public List<FlightEntity> getFlights(String depIata, String arrIata) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("access_key", API_KEY)
                .queryParam("dep_iata", depIata)
                .queryParam("arr_iata", arrIata)
                .toUriString();
        Map response = restTemplate.getForObject(url, Map.class);
        List<FlightEntity> flights = new ArrayList<>();
        if (response != null && response.containsKey("data")) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
            for (Map<String, Object> flight : data) {
                Map<String, Object> flightInfo = (Map<String, Object>) flight.get("flight");
                Map<String, Object> airlineInfo = (Map<String, Object>) flight.get("airline");
                Map<String, Object> departure = (Map<String, Object>) flight.get("departure");
                Map<String, Object> arrival = (Map<String, Object>) flight.get("arrival");
                flights.add(new FlightEntity(
                    flightInfo != null ? (String) flightInfo.get("iata") : null,
                    departure != null ? (String) departure.get("iata") : null,
                    arrival != null ? (String) arrival.get("iata") : null,
                    (String) flight.get("flight_date"),
                    airlineInfo != null ? (String) airlineInfo.get("name") : null,
                    (String) flight.get("flight_status")
                ));
            }
        }
        return flights;
    }
} 