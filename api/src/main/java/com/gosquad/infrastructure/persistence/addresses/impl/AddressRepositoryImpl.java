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

@org.springframework.stereotype.Repository
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
                rs.getInt("city_id")
        );
    }

    public AddressModel getByAddressLineByCityId(String addressLine, int cityId) throws SQLException, NotFoundException {
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("address_line", addressLine);
            conditions.put("city_id", cityId);

            return findByMultiple(conditions);

    }

    @Override
    public void addAddress(AddressModel address) throws SQLException {
        try {
            Map<String, Object> values = new HashMap<>();
            values.put("address_line", address.getAddressLine());
            values.put("city_id", address.getCityId());
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

            updateBy("id",address.getId(),updates);
        }catch (SQLException e) {
            throw new ConstraintViolationException(e);
        }
    }
}
