package com.gosquad.presentation.controller;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.domain.cities.CityEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.presentation.DTO.CustomerRequestDTO;
import com.gosquad.usecase.addresses.AddressService;
import com.gosquad.usecase.cities.CityService;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.countries.CountryService;
import com.gosquad.usecase.customers.CustomerService;
import com.gosquad.usecase.files.FileService;
import com.gosquad.usecase.security.EncryptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
public class CustomerController {

    private final EncryptionService encryptionService;
    private final FileService fileService;
    private final CountryService countryService;
    private final AddressService addressService;
    private final CityService cityService;
    private final CustomerService customerService;
    private final CompanyService companyService;

    public CustomerController(EncryptionService encryptionService, FileService fileService, CountryService countryService, AddressService addressService, CityService cityService, CustomerService customerService, CompanyService companyService) {
        this.encryptionService = encryptionService;
        this.fileService = fileService;
        this.countryService = countryService;
        this.addressService = addressService;
        this.cityService = cityService;
        this.customerService = customerService;
        this.companyService = companyService;
    }

    private void validateIsoCode(String code, String fieldName) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Le " + fieldName + " ne peut pas être null ou vide");
        }
    }
    private CityEntity getOrCreateCity(String cityName, String postalCode, Integer countryId) throws SQLException, NotFoundException, ConstraintViolationException {
        try {
            return cityService.getCityByNameByPostalCodeByCountry(cityName, postalCode, countryId);
        } catch (NotFoundException | ConstraintViolationException e) {
            CityEntity newCity = new CityEntity(null, cityName, postalCode, countryId);
            cityService.addCity(newCity);
            return cityService.getCityByNameByPostalCodeByCountry(cityName, postalCode, countryId);
        }
    }

    private AddressEntity getOrCreateAddress(String addressLine, Integer cityId, Integer countryId) throws SQLException, NotFoundException, ConstraintViolationException {
        try {
            return addressService.getAddressByAddressLineByCityIdByCountryId(addressLine, cityId, countryId);
        } catch (NotFoundException | ConstraintViolationException e) {
            AddressEntity newAddress = new AddressEntity(null, addressLine, cityId, countryId);
            addressService.addAddress(newAddress);
            return addressService.getAddressByAddressLineByCityIdByCountryId(addressLine, cityId, countryId);
        }
    }



    @PostMapping("/customer")
    public ResponseEntity<String> addCustomer(@RequestPart("customer") CustomerRequestDTO customerRequestDTO,
                                              @RequestPart(value = "idCard", required = false) MultipartFile idCard,
                                              @RequestPart(value = "passport", required = false) MultipartFile passport) {
        try {

            validateIsoCode(customerRequestDTO.getIsoNationality(), "code ISO de nationalité");
            validateIsoCode(customerRequestDTO.getIsoCode(), "code ISO d'adresse");
            validateIsoCode(customerRequestDTO.getIsoCodeBilling(), "code ISO de facturation");

            CompanyEntity companyEntity = companyService.getCompanyByCode(customerRequestDTO.getCompanyCode());

            CountryEntity countryEntity = countryService.getCountryByIsoCode(customerRequestDTO.getIsoNationality());
            CountryEntity countryEntityForAddress = countryService.getCountryByIsoCode(customerRequestDTO.getIsoCode());
            CountryEntity countryEntityForAddressBilling = countryService.getCountryByIsoCode(customerRequestDTO.getIsoCodeBilling());

            // Utilisation de getOrCreateCity pour la ville principale
            CityEntity cityEntity = getOrCreateCity(
                    customerRequestDTO.getCityName(),
                    customerRequestDTO.getPostalCode(),
                    countryEntityForAddress.getId()
            );

            // Utilisation de getOrCreateCity pour la ville de facturation
            CityEntity cityEntityForBillingAddress = getOrCreateCity(
                    customerRequestDTO.getCityNameBilling(),
                    customerRequestDTO.getPostalCodeBilling(),
                    countryEntityForAddressBilling.getId()
            );

            // Adresse principale
            AddressEntity addressEntity = getOrCreateAddress(
                    customerRequestDTO.getAddressLine(),
                    cityEntity.getId(),
                    countryEntityForAddress.getId()
            );

            // Adresse de facturation
            AddressEntity addressEntityForBillingAddress = getOrCreateAddress(
                    customerRequestDTO.getAddressLineBilling(),
                    cityEntityForBillingAddress.getId(),
                    countryEntityForAddressBilling.getId()
            );

            CustomerEntity customerEntity = new CustomerEntity(
                    null,
                    customerRequestDTO.getFirstName(),
                    customerRequestDTO.getLastName(),
                    customerRequestDTO.getEmail(),
                    customerRequestDTO.getPhoneNumber(),
                    customerRequestDTO.getBirthDate(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    countryEntity.getId(),
                    addressEntity.getId(),
                    addressEntityForBillingAddress.getId(),
                    companyEntity.getId()
            );


            boolean hasIdCardData = Stream.of(
                    customerRequestDTO.getIdCardNumber(),
                    customerRequestDTO.getIdCardExpirationDate(),
                    idCard.getBytes()
            ).allMatch(Objects::nonNull);


            if (hasIdCardData) {
                byte[] processedIdCard = fileService.processFile(idCard.getBytes());
                byte[] encryptedIdCard = encryptionService.encrypt(processedIdCard);
                String idCardUrl = fileService.uploadFileImage(encryptedIdCard);
                customerEntity.setIdCardCopyUrl(idCardUrl);
                customerEntity.setIdCardNumber(encryptionService.encrypt(customerRequestDTO.getIdCardNumber()));
                customerEntity.setIdCardExpirationDate(customerRequestDTO.getIdCardExpirationDate());
            }


            boolean hasPassportData = Stream.of(
                    passport.getBytes(),
                    customerRequestDTO.getPassportNumber(),
                    customerRequestDTO.getPassportExpirationDate()
            ).allMatch(Objects::nonNull);

            if (hasPassportData) {
                byte[] processedPassport = fileService.processFile(passport.getBytes());
                byte[] encryptedPassport = encryptionService.encrypt(processedPassport);
                String passportUrl = fileService.uploadFileImage(encryptedPassport);
                customerEntity.setPassportCopyUrl(passportUrl);
                customerEntity.setPassportNumber(encryptionService.encrypt(customerRequestDTO.getPassportNumber()));
                customerEntity.setPassportExpirationDate(customerRequestDTO.getPassportExpirationDate());
            }

            customerService.addCustomer(customerEntity);

            return ResponseEntity.ok("Client ajouté avec succès");

        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Ressource non trouvée : " + e.getMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Violation de contrainte : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erreur de requête : " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lors du traitement du fichier : " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Erreur SQL : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur inattendue : " + e.getMessage());
        }
    }


}
