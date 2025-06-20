package com.gosquad.presentation.controller;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.presentation.DTO.CountryRequestDTO;
import com.gosquad.usecase.countries.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/country/{isoCode}")
    public ResponseEntity<Object> getCountry(@PathVariable String isoCode) {
        try {
            countryService.getCountryByIsoCode(isoCode);
            return new ResponseEntity<>(countryService.getCountryByIsoCode(isoCode), HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>("Erreur interne du serveur", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("Code iso n'existe pas", HttpStatus.NOT_FOUND);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>("Requête invalide", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/country")
    public ResponseEntity<String> addCountry(@RequestBody CountryRequestDTO body) {
        try {
            countryService.addCountry(new CountryEntity(null, body.isoCode(), body.countryName()));
            return ResponseEntity.status(HttpStatus.CREATED).body("Country created successfully");

        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Database error: " + e.getMessage());
        }
    }
}
