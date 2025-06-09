package com.gosquad.infrastructure.persistence.cities.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.cities.CityModel;
import com.gosquad.infrastructure.persistence.cities.CityRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CityRepositoryImpl extends Repository<CityModel> implements CityRepository {
    public static final String TABLE_NAME = "cities";

    public CityRepositoryImpl() throws SQLException {
        super(TABLE_NAME);
    }

    @Override
    protected CityModel mapResultSetToEntity(ResultSet rs) throws SQLException{
        return new CityModel(
                rs.getInt("id"),
                rs.getString("city_name"),
                rs.getString("postal_code"),
                rs.getInt("country_id")
        );
    }

    public CityModel getByNameByPostalCodeByCountry(String name,String postalCode,int countryId) throws SQLException, ConstraintViolationException {
        try {
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("city_name", name);
            conditions.put("postal_code", postalCode);
            conditions.put("country_id", countryId);

            return findByMultiple(conditions);
        }catch (NotFoundException e) {
            throw new ConstraintViolationException(e);
        }
    }


    public void addCity(CityModel city) throws SQLException{
        try {
            Map<String, Object> values = new HashMap<>();
            values.put("city_name", city.getCityName());
            values.put("postal_code", city.getPostalCode());
            values.put("country_id", city.getCountryId());
            city.setId(insert(values));
        }catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateCity(CityModel city) throws ConstraintViolationException {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("city_name  ", city.getCityName());
            updates.put("postal_code", city.getPostalCode());
            updates.put("country_id ", city.getCountryId());

            updateBy("id",city.getId(),updates);
        }catch (SQLException e) {
            throw new ConstraintViolationException(e);
        }
    }

}
