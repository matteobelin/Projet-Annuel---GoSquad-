package com.gosquad.domain.customers;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.regex.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerEntity extends Entity {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{7,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    String firstname;
    String lastname;
    String email;
    String phoneNumber;
    Date birthDate;

    String idCardNumber;
    Date idCardExpirationDate;
    String idCardCopyUrl;

    String passportNumber;
    Date passportExpirationDate;
    String passportCopyUrl;

    Integer countryId;
    Integer addressId;
    Integer billingAddressId;

    Integer companyId;

    public CustomerEntity(Integer id,String firstname,String lastname,String email, String phoneNumber
            , Date birthDate, String idCardNumber, Date idCardExpirationDate, String idCardCopyUrl, String passportNumber,
                         Date passportExpirationDate, String passportCopyUrl, Integer countryId, Integer addressId,Integer billingAddressId,
                         Integer companyId) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;

        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        this.email = email;

        if (phoneNumber == null || !PHONE_PATTERN.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.idCardNumber = idCardNumber;
        this.idCardExpirationDate = idCardExpirationDate;
        this.idCardCopyUrl = idCardCopyUrl;
        this.passportNumber = passportNumber;
        this.passportExpirationDate = passportExpirationDate;
        this.passportCopyUrl = passportCopyUrl;
        this.countryId = countryId;
        this.addressId = addressId;
        this.billingAddressId = billingAddressId;
        this.companyId = companyId;
    }
}
