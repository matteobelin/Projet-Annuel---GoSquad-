package com.gosquad.presentation.controller;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.cities.CityEntity;
import com.gosquad.presentation.DTO.CityRequestDTO;
import com.gosquad.usecase.cities.CityService;
import com.gosquad.usecase.countries.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;

@Controller
public class CitiesController {

    private final CountryService countryService;
    private final CityService cityService;

    public CitiesController(CountryService countryService, CityService cityService) {
        this.countryService = countryService;
        this.cityService = cityService;
    }

    @PostMapping("/city")
    public ResponseEntity<String> addCountry(@RequestBody CityRequestDTO body) {
        try {
            int countryId = countryService.getCountryByIsoCode(body.isoCode()).getId();
            cityService.addCity(new CityEntity(null, body.cityName(), body.postalCode(), countryId));
            return ResponseEntity.status(HttpStatus.CREATED).body("City created successfully");
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Database error: " + e.getMessage());
        } catch (NotFoundException e) {
            return new ResponseEntity<>("Code iso n'existe pas", HttpStatus.NOT_FOUND);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>("Requête invalide", HttpStatus.BAD_REQUEST);
        }
    }

}
