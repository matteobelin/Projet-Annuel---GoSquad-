package com.gosquad.usecase.addresses;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.addresses.AddressEntity;

import java.sql.SQLException;

public interface AddressService {
    AddressEntity getAddressByID(int id) throws SQLException, NotFoundException;
    AddressEntity getAddressByAddressLineByCityId(String addressLine, int cityId) throws SQLException, NotFoundException, ConstraintViolationException;
    void addAddress(AddressEntity address) throws SQLException;
    void updateAddress(AddressEntity address) throws SQLException, ConstraintViolationException;
    AddressEntity getOrCreateAddress(String addressLine, Integer cityId) throws SQLException, NotFoundException, ConstraintViolationException;
}
