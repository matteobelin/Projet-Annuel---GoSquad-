package com.gosquad.usecase.addresses;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.addresses.AddressEntity;

import java.sql.SQLException;

public interface AddressService {
    AddressEntity getAddressByID(int id) throws SQLException, NotFoundException;
    AddressEntity getAddressByAddressLineByCityIdByCountryId(String addressLine, int cityId, int countryId) throws SQLException, NotFoundException, ConstraintViolationException;
    void addAddress(AddressEntity address) throws SQLException;
    void updateAddress(AddressEntity address) throws SQLException, ConstraintViolationException;
}
