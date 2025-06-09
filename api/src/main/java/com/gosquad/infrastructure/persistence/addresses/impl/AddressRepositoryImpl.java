package com.gosquad.infrastructure.persistence.addresses.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.addresses.AddressModel;
import com.gosquad.infrastructure.persistence.addresses.AddressRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AddressRepositoryImpl extends Repository<AddressModel> implements AddressRepository {
    public static final String TABLE_NAME = "addresses";

    public AddressRepositoryImpl() throws SQLException {
        super(TABLE_NAME);
    }

    @Override
    protected AddressModel mapResultSetToEntity(ResultSet rs) throws SQLException{
        return new AddressModel(
                rs.getInt("id"),
                rs.getString("address_line"),
                rs.getInt("city_id"),
                rs.getInt("country_id")
        );
    }

    public AddressModel getByAddressLineByCityIdByCountryId(String addressLine, int cityId, int countryId) throws SQLException,ConstraintViolationException {
        try {
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("address_line", addressLine);
            conditions.put("city_id", cityId);
            conditions.put("country_id", countryId);

            return findByMultiple(conditions);
        }catch (NotFoundException e) {
            throw new ConstraintViolationException(e);
        }
    }

    @Override
    public void addAddress(AddressModel address) throws SQLException {
        try {
            Map<String, Object> values = new HashMap<>();
            values.put("address_line", address.getAddressLine());
            values.put("city_id", address.getCityId());
            values.put("country_id", address.getCountryId());
            address.setId(insert(values));
        }catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateAddress(AddressModel address) throws SQLException, ConstraintViolationException {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("address_line", address.getAddressLine());
            updates.put("city_id", address.getCityId());
            updates.put("country_id", address.getCountryId());

            updateBy("id",address.getId(),updates);
        }catch (SQLException e) {
            throw new ConstraintViolationException(e);
        }
    }
}
