package com.gosquad.infrastructure.persistence.addresses;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;

import java.sql.SQLException;


public interface AddressRepository {
    AddressModel getById(int id) throws SQLException, NotFoundException;
    AddressModel getByAddressLineByCityId(String addressLine, int cityId) throws SQLException, ConstraintViolationException, NotFoundException;
    void addAddress(AddressModel address) throws SQLException;
    void updateAddress(AddressModel address) throws SQLException, ConstraintViolationException;
}
