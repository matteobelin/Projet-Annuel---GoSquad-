package com.gosquad.domain.customers;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerEntity extends Entity {
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
    String customer_number;

    public CustomerEntity(Integer id,String firstname,String lastname,String email, String phoneNumber
            , Date birthDate, String idCardNumber, Date idCardExpirationDate, String idCardCopyUrl, String passportNumber,
                         Date passportExpirationDate, String passportCopyUrl, Integer countryId, Integer addressId,Integer billingAddressId,
                         Integer companyId, String customer_number) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
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
        this.customer_number = customer_number;
    }
}
