package com.gosquad.presentation.controller.customers;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.domain.cities.CityEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.countries.CountryEntity;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.presentation.DTO.customers.CustomerUpdateDTO;
import com.gosquad.usecase.addresses.AddressService;
import com.gosquad.usecase.cities.CityService;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.countries.CountryService;
import com.gosquad.usecase.customers.CustomerService;
import com.gosquad.usecase.files.FileService;
import com.gosquad.usecase.security.EncryptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;


@Controller
public class CustomerUpdateController {
    private final EncryptionService encryptionService;
    private final FileService fileService;
    private final CountryService countryService;
    private final AddressService addressService;
    private final CityService cityService;
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final JWTInterceptor jwtInterceptor;

    public CustomerUpdateController(EncryptionService encryptionService, FileService fileService, CountryService countryService, AddressService addressService, CityService cityService, CustomerService customerService, CompanyService companyService, JWTInterceptor jwtInterceptor) {
        this.encryptionService = encryptionService;
        this.fileService = fileService;
        this.countryService = countryService;
        this.addressService = addressService;
        this.cityService = cityService;
        this.customerService = customerService;
        this.companyService = companyService;
        this.jwtInterceptor = jwtInterceptor;
    }


    private void validateIsoCode(String code, String fieldName) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Le " + fieldName + " ne peut pas être null ou vide");
        }
    }
    @PutMapping("/updateCustomer")
    public ResponseEntity<?> updateCustomer(HttpServletRequest request,@RequestBody CustomerUpdateDTO body) {
        try {

            validateIsoCode(body.getIsoNationality(), "code ISO de nationalité");
            validateIsoCode(body.getIsoCode(), "code ISO d'adresse");
            validateIsoCode(body.getIsoCodeBilling(), "code ISO de facturation");

            String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(7);
            Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);

            String companyCode = tokenInfo.get("companyCode").toString();
            int customerId = Integer.parseInt(body.getUniqueCustomerId().replaceAll(companyCode, ""));
            CompanyEntity company = companyService.getCompanyByCode(companyCode);

            CustomerEntity customer = customerService.getCustomerByIdAndCompanyId(customerId, company.getId());

            CompanyEntity companyEntity = companyService.getCompanyByCode(body.getCompanyCode());

            CountryEntity countryEntity = countryService.getCountryByIsoCode(body.getIsoNationality());
            CountryEntity countryEntityForAddress = countryService.getCountryByIsoCode(body.getIsoCode());
            CountryEntity countryEntityForAddressBilling = countryService.getCountryByIsoCode(body.getIsoCodeBilling());

            // Utilisation de getOrCreateCity pour la ville principale
            CityEntity cityEntity = cityService.getOrCreateCity(
                    body.getCityName(),
                    body.getPostalCode(),
                    countryEntityForAddress.getId()
            );

            // Utilisation de getOrCreateCity pour la ville de facturation
            CityEntity cityEntityForBillingAddress = cityService.getOrCreateCity(
                    body.getCityNameBilling(),
                    body.getPostalCodeBilling(),
                    countryEntityForAddressBilling.getId()
            );

            // Adresse principale
            AddressEntity addressEntity = addressService.getOrCreateAddress(
                    body.getAddressLine(),
                    cityEntity.getId(),
                    countryEntityForAddress.getId()
            );

            // Adresse de facturation
            AddressEntity addressEntityForBillingAddress = addressService.getOrCreateAddress(
                    body.getAddressLineBilling(),
                    cityEntityForBillingAddress.getId(),
                    countryEntityForAddressBilling.getId()
            );

            CustomerEntity customerEntity = new CustomerEntity(
                    null,
                    body.getFirstName(),
                    body.getLastName(),
                    body.getEmail(),
                    body.getPhoneNumber(),
                    body.getBirthDate(),
                    customer.getIdCardNumber(),
                    customer.getIdCardExpirationDate(),
                    customer.getIdCardCopyUrl(),
                    customer.getPassportNumber(),
                    customer.getPassportExpirationDate(),
                    customer.getPassportCopyUrl(),
                    countryEntity.getId(),
                    addressEntity.getId(),
                    addressEntityForBillingAddress.getId(),
                    companyEntity.getId()
            );

            customerService.updateCustomer(customerEntity);

            return ResponseEntity.ok("Customer updated successfully");
        }  catch (SQLException e) {
            return ResponseEntity.status(500).body("Erreur SQL : " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Ressource non trouvée : " + e.getMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Violation de contrainte : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur : " + e.getMessage());
        }
    }
    @PutMapping("/updateCustomerPassport")
    public ResponseEntity<?> updateCustomerPassport(HttpServletRequest request,@RequestPart("customer") CustomerUpdateDTO customerUpdateDTO, @RequestPart(value = "passport", required = true) org.springframework.web.multipart.MultipartFile passport) {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(7);
            Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);

            String companyCode = tokenInfo.get("companyCode").toString();
            int customerId = Integer.parseInt(customerUpdateDTO.getUniqueCustomerId().replaceAll(companyCode, ""));
            CompanyEntity company = companyService.getCompanyByCode(companyCode);

            CustomerEntity customer = customerService.getCustomerByIdAndCompanyId(customerId, company.getId());

            boolean hasPassportData = Stream.of(
                passport.getBytes(),
                customerUpdateDTO.getPassportNumber(),
                customerUpdateDTO.getPassportExpirationDate()
            ).allMatch(Objects::nonNull);

            if(!hasPassportData) {
                return ResponseEntity.badRequest().body("Les données du passeport sont incomplètes");
            }
            if(customer.getPassportCopyUrl() != null) {
                fileService.deleteFileImage(customer.getPassportCopyUrl());
            }
            byte[] processedPassport = fileService.processFile(passport.getBytes());
            byte[] encryptedPassport = encryptionService.encrypt(processedPassport);
            String passportUrl = fileService.uploadFileImage(encryptedPassport);

            customer.setPassportCopyUrl(passportUrl);
            customer.setPassportNumber(encryptionService.encrypt(customerUpdateDTO.getPassportNumber()));
            customer.setPassportExpirationDate(customerUpdateDTO.getPassportExpirationDate());

            customerService.updateCustomerPassport(customer);
            return ResponseEntity.ok("Customer passport updated successfully");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Erreur SQL : " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Ressource non trouvée : " + e.getMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Violation de contrainte : " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur d'entrée/sortie : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur : " + e.getMessage());
        }

    }

    @PutMapping("/updateCustomerIdCard")
    public ResponseEntity<?> updateCustomerIdCard(HttpServletRequest request,@RequestPart("customer") CustomerUpdateDTO customerUpdateDTO, @RequestPart(value = "idCard", required = true) org.springframework.web.multipart.MultipartFile idCard) {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(7);
            Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);

            String companyCode = tokenInfo.get("companyCode").toString();
            int customerId = Integer.parseInt(customerUpdateDTO.getUniqueCustomerId().replaceAll(companyCode, ""));
            CompanyEntity company = companyService.getCompanyByCode(companyCode);

            CustomerEntity customer = customerService.getCustomerByIdAndCompanyId(customerId, company.getId());

            boolean hasidCardData = Stream.of(
                    idCard.getBytes(),
                    customerUpdateDTO.getIdCardNumber(),
                    customerUpdateDTO.getIdCardExpirationDate()
            ).allMatch(Objects::nonNull);

            if(!hasidCardData) {
                return ResponseEntity.badRequest().body("Les données de la carte d'identité sont incomplètes");
            }
            if(customer.getIdCardCopyUrl() != null) {
                fileService.deleteFileImage(customer.getIdCardCopyUrl());
            }
            byte[] processedIdCard = fileService.processFile(idCard.getBytes());
            byte[] encryptedIdCard = encryptionService.encrypt(processedIdCard);
            String idCardUrl = fileService.uploadFileImage(encryptedIdCard);

            customer.setIdCardCopyUrl(idCardUrl);
            customer.setIdCardNumber(encryptionService.encrypt(customerUpdateDTO.getPassportNumber()));
            customer.setIdCardExpirationDate(customerUpdateDTO.getPassportExpirationDate());

            customerService.updateCustomerIdCard(customer);
            return ResponseEntity.ok("Customer passport updated successfully");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Erreur SQL : " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Ressource non trouvée : " + e.getMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Violation de contrainte : " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur d'entrée/sortie : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur : " + e.getMessage());
        }
    }

    @PatchMapping("/updateCustomerToAnonymous")
    public ResponseEntity<?> updateCustomerToAnonymous(HttpServletRequest request,@RequestBody CustomerUpdateDTO body) {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(7);
            Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);

            String companyCode = tokenInfo.get("companyCode").toString();
            int customerId = Integer.parseInt(body.getUniqueCustomerId().replaceAll(companyCode, ""));
            CompanyEntity company = companyService.getCompanyByCode(companyCode);

            CustomerEntity customer = customerService.getCustomerByIdAndCompanyId(customerId, company.getId());

            if (customer.getPassportCopyUrl() != null) {
                try {
                    fileService.deleteFileImage(customer.getPassportCopyUrl());
                } catch (IOException e) {
                    return ResponseEntity.status(500).body("Erreur lors de la suppression de l'image du passeport : " + e.getMessage());
                }
            }
            if (customer.getIdCardCopyUrl() != null) {
                try {
                    fileService.deleteFileImage(customer.getIdCardCopyUrl());
                } catch (IOException e) {
                    return ResponseEntity.status(500).body("Erreur lors de la suppression de l'image de la carte d'identité : " + e.getMessage());
                }
            }

            customer.setPassportExpirationDate(null);
            customer.setPassportNumber(null);
            customer.setPassportCopyUrl(null);
            customer.setIdCardExpirationDate(null);
            customer.setIdCardNumber(null);
            customer.setIdCardCopyUrl(null);
            customer.setFirstname("anonym");
            customer.setLastname("anonym");
            customer.setEmail("anonym@gmail.com");
            customer.setPhoneNumber("0000000000");
            customer.setBirthDate(Date.valueOf(LocalDate.of(1900, 1, 1)));
            customer.setCountryId(null);
            customer.setAddressId(null);
            customer.setBillingAddressId(null);

            customerService.updateCustomer(customer);



            return ResponseEntity.ok("Customer updated to anonymous successfully");
        }catch (SQLException e) {
            return ResponseEntity.status(500).body("Erreur SQL : " + e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Ressource non trouvée : " + e.getMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Violation de contrainte : " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur d'entrée/sortie : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur : " + e.getMessage());
        }

    }
}
