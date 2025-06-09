package com.gosquad.infrastructure.persistence.countries.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.Repository;
import com.gosquad.infrastructure.persistence.countries.CountryModel;
import com.gosquad.infrastructure.persistence.countries.CountryRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Repository
public class CountryRepositoryImpl extends Repository<CountryModel> implements CountryRepository {

    public static final String TABLE_NAME = "countries";

    public CountryRepositoryImpl() throws SQLException {
        super(TABLE_NAME);
    }

    @Override
    protected CountryModel mapResultSetToEntity(ResultSet rs) throws SQLException{
        return new CountryModel(
                rs.getInt("id"),
                rs.getString("iso_code"),
                rs.getString("country_name")
        );
    }

    public CountryModel getByIsoCode(String isoCode) throws SQLException, ConstraintViolationException {
        try{
            return findBy("iso_code",isoCode,"id","iso_code","country_name");
        }catch (NotFoundException e){
            throw new ConstraintViolationException(e);
        }
    }

    public void addCountry(CountryModel country) throws SQLException{
        try {
            Map<String, Object> values = new HashMap<>();
            values.put("iso_code", country.getIsoCode());
            values.put("country_name", country.getCountryName());

            country.setId(insert(values));
        }catch (SQLException e){
            throw new SQLException(e);
        }
    }
}
