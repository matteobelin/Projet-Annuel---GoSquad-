package com.gosquad.presentation.controller;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.presentation.DTO.AddressRequestDTO;
import com.gosquad.usecase.addresses.AddressService;
import com.gosquad.usecase.cities.CityService;
import com.gosquad.usecase.countries.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
public class AddressController {
    private final AddressService addressService;
    private final CityService cityService;
    private final CountryService countryService;

    public AddressController(AddressService addressService, CityService cityService, CountryService countryService) {
        this.addressService = addressService;
        this.cityService = cityService;
        this.countryService = countryService;
    }

    @PostMapping("/address")
    public ResponseEntity<String> addAddress(@RequestParam AddressRequestDTO body) {
        try{
            int countryId = countryService.getCountryByIsoCode(body.isoCode()).getId();
            int cityId = cityService.getCityByNameByPostalCodeByCountry(body.cityName(), body.postalCode(), countryId).getId();
            addressService.addAddress(new AddressEntity(null, body.address_line(),cityId));

            return ResponseEntity.status(HttpStatus.CREATED).body("address created successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }catch (NotFoundException e) {
            return new ResponseEntity<>("City or Iso Code not exist", HttpStatus.NOT_FOUND);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>("Requête invalide", HttpStatus.BAD_REQUEST);
        }
    }
}
